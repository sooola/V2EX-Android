package com.sola.v2ex_android.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpCookie;

/**
 * Created by wei on 2016/11/11.
 */

public class SerializableHttpCookie implements Serializable {
    private static final long SERIAL_VERSION_UID = 6374381323722046732L;

    private transient final HttpCookie mCookie;
    private transient HttpCookie mClientCookie;

    public SerializableHttpCookie(HttpCookie cookie) {
        this.mCookie = cookie;
    }

    public HttpCookie getmCookie() {
        HttpCookie bestCookie = mCookie;
        if (mClientCookie != null) {
            bestCookie = mClientCookie;
        }
        return bestCookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(mCookie.getName());
        out.writeObject(mCookie.getValue());
        out.writeObject(mCookie.getComment());
        out.writeObject(mCookie.getCommentURL());
        out.writeObject(mCookie.getDomain());
        out.writeLong(mCookie.getMaxAge());
        out.writeObject(mCookie.getPath());
        out.writeObject(mCookie.getPortlist());
        out.writeInt(mCookie.getVersion());
        out.writeBoolean(mCookie.getSecure());
        out.writeBoolean(mCookie.getDiscard());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        mClientCookie = new HttpCookie(name, value);
        mClientCookie.setComment((String) in.readObject());
        mClientCookie.setCommentURL((String) in.readObject());
        mClientCookie.setDomain((String) in.readObject());
        mClientCookie.setMaxAge(in.readLong());
        mClientCookie.setPath((String) in.readObject());
        mClientCookie.setPortlist((String) in.readObject());
        mClientCookie.setVersion(in.readInt());
        mClientCookie.setSecure(in.readBoolean());
        mClientCookie.setDiscard(in.readBoolean());
    }

}
