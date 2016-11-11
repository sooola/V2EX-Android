package com.sola.v2ex_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wei on 2016/11/11.
 */

public class PersistentCookieStore implements CookieStore {

    public static final String TAG = PersistentCookieStore.class.getName();
    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_PREFIX = "cookie_";

    private final HashMap<String, ConcurrentHashMap<String, HttpCookie>> mCookies;
    private final SharedPreferences mCookiePrefs;

    /**
     * Construct a persistent cookie store.
     *
     * @param context Context to attach cookie store to
     */
    public PersistentCookieStore(Context context) {
        mCookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
        mCookies = new HashMap<>();

        // Load any previously stored mCookies into the store
        Map<String, ?> prefsMap = mCookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if (entry.getValue() != null && !((String) entry.getValue()).startsWith(COOKIE_NAME_PREFIX)) {
                String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
                for (String name : cookieNames) {
                    String encodedCookie = mCookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        HttpCookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if (!mCookies.containsKey(entry.getKey()))
                                mCookies.put(entry.getKey(), new ConcurrentHashMap<String, HttpCookie>());
                            mCookies.get(entry.getKey()).put(name, decodedCookie);
                        }
                    }
                }

            }
        }
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        // Save cookie into local store, or remove if expired
        if (!cookie.hasExpired()) {
            if (!mCookies.containsKey(cookie.getDomain()))
                mCookies.put(cookie.getDomain(), new ConcurrentHashMap<String, HttpCookie>());
            mCookies.get(cookie.getDomain()).put(cookie.getName(), cookie);
        } else {
            if (mCookies.containsKey(cookie.getDomain()))
                mCookies.get(cookie.getDomain()).remove(cookie.getDomain());
        }

        TokenCache.saveToken(cookie.getValue());
        // Save cookie into persistent store
        SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
        prefsWriter.putString(cookie.getDomain(), TextUtils.join(",", mCookies.get(cookie.getDomain()).keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + cookie.getName(), encodeCookie(new SerializableHttpCookie(cookie)));
        prefsWriter.apply();

    }

    protected String getCookieToken(URI uri, HttpCookie cookie) {
        String cookieToken = cookie.getName() + cookie.getDomain();
        return cookieToken;
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        ArrayList<HttpCookie> ret = new ArrayList<>();
        for (String key : mCookies.keySet()) {
            if (uri.getHost().contains(key)) {
                ret.addAll(mCookies.get(key).values());
            }
        }
        return ret;
    }

    @Override
    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        mCookies.clear();
        return true;
    }


    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        String name = getCookieToken(uri, cookie);

        if (mCookies.containsKey(uri.getHost()) && mCookies.get(uri.getHost()).containsKey(name)) {
            mCookies.get(uri.getHost()).remove(name);

            SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
            if (mCookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);
            }
            prefsWriter.putString(uri.getHost(), TextUtils.join(",", mCookies.get(uri.getHost()).keySet()));
            prefsWriter.apply();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<HttpCookie> getCookies() {
        ArrayList<HttpCookie> ret = new ArrayList<>();
        for (String key : mCookies.keySet()) {
            ret.addAll(mCookies.get(key).values());
            android.util.Log.d(TAG, key + " : " + mCookies.keySet());
        }
        return ret;
    }

    @Override
    public List<URI> getURIs() {
        ArrayList<URI> ret = new ArrayList<>();
        for (String key : mCookies.keySet())
            try {
                ret.add(new URI(key));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        return ret;
    }

    /**
     * Serializes Cookie object into String
     *
     * @param cookie cookie to be encoded, can be null
     * @return cookie encoded as String
     */
    protected String encodeCookie(SerializableHttpCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e);
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * Returns cookie decoded from cookie string
     *
     * @param cookieString string of cookie as returned from http request
     * @return decoded cookie or null if exception occured
     */
    protected HttpCookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        HttpCookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).getmCookie();
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
        }

        return cookie;
    }

    /**
     * Using some super basic byte array <-> hex conversions so we don't have to rely on any
     * large Base64 libraries. Can be overridden if you like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * Converts hex values from strings to byte arra
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

}
