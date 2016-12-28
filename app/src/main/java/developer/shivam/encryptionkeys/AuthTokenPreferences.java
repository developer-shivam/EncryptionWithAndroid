package developer.shivam.encryptionkeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

class AuthTokenPreferences implements SharedPreferences {

    private SharedPreferences delegate;
    private Context mContext;
    private char[] PASSWORD = "shivamthegenius".toCharArray();
    private String UTF8 = "utf-8";

    public AuthTokenPreferences(Context context, SharedPreferences sharedPreferences) {
        this.delegate = sharedPreferences;
        this.mContext = context;
    }

    private class Editor implements SharedPreferences.Editor {

        SharedPreferences.Editor delegate;

        Editor() {
            delegate = AuthTokenPreferences.this.delegate.edit();
        }

        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            delegate.putString(key, encrypt(value));
            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String s, Set<String> set) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            delegate.putString(key, encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            delegate.putString(key, encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            delegate.putString(key, encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            delegate.putString(key, encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            delegate.remove(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            delegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return delegate.commit();
        }

        @Override
        public void apply() {
            delegate.apply();
        }
    }

    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        String value = delegate.getString(key, null);
        return value != null ? decrypt(value) : defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String s, Set<String> set) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getInt(String key, int defValue) {
        String value = delegate.getString(key, null);
        return value != null ? Integer.parseInt(decrypt(value)) : defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        String value = delegate.getString(key, null);
        return value != null ? Long.parseLong(decrypt(value)) : defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        String value = delegate.getString(key, null);
        return value != null ? Float.parseFloat(decrypt(value)) : defValue;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        String value = delegate.getString(key, null);
        return value != null ? Boolean.parseBoolean(decrypt(value)) : defValue;
    }

    @Override
    public boolean contains(String key) {
        return delegate.contains(key);
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

    }

    private String encrypt(String value) {
        try {
            //Converting the value to byte array
            final byte[] bytes = value != null ? value.getBytes(UTF8) : new byte[0];
            //Secret key factory with MD5 and DES
            SecretKeyFactory mSecretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            //Generating secret key with password
            SecretKey mSecretKey = mSecretKeyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher mCipher = Cipher.getInstance("PBEWithMD5AndDES");
            mCipher.init(Cipher.ENCRYPT_MODE, mSecretKey, new PBEParameterSpec(Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(UTF8), 20));
            //Returning the encrypted string to save in sharedPreferences.
            String encryptedString = new String(Base64.encode(mCipher.doFinal(bytes), Base64.NO_WRAP),UTF8);
            System.out.println(encryptedString);
            return encryptedString;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String decrypt(String value) {
        try {
            //Converting the value to byte array
            final byte[] bytes = value != null ? value.getBytes(UTF8) : new byte[0];
            //Secret key factory with MD5 and DES
            SecretKeyFactory mSecretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            //Generating secret key with password
            SecretKey mSecretKey = mSecretKeyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher mCipher = Cipher.getInstance("PBEWithMD5AndDES");
            mCipher.init(Cipher.DECRYPT_MODE, mSecretKey, new PBEParameterSpec(Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(UTF8), 20));
            //Returning the encrypted string to save in sharedPreferences.
            String decryptedString = new String(mCipher.doFinal(bytes),UTF8);
            System.out.println(decryptedString);
            return decryptedString;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
