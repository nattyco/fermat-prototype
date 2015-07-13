package com.bitdubai.fermat_osa_addon.layer.android.file_system.developer.bitdubai.version_1.structure;

import android.content.Context;
import android.util.Base64;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by ciencias on 20.01.15.
 */

/**
 * The Plugin File System is the implementation of the file system that is handled to external plu256gins. It differs
 * from the Platform File System in that this one requires the plug in to identify itself.
 *
 */

public class AndroidPluginFileSystem implements PluginFileSystem {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String DIGEST_ALGORITHM = "SHA-256";

    /**
     * PluginFileSystem interface member variables.
     */
    
    private Context context;

    /**
     * PluginFileSystem interface implementation.
     */

    /**
     *<p>This method return a PluginTextFile object and load file content on memory
     *
     * @param ownerId  PlugIn id
     * @param directoryName name of the directory where the files are stored
     * @param fileName name of file to load
     * @param privacyLevel level of privacy for the file, if it is public or private
     * @param lifeSpan lifeSpan of the file, whether it is permanent or temporary
     * @return PluginTextFile object
     * @throws FileNotFoundException
     * @throws CantCreateFileException
     */
    @Override
    public PluginTextFile getTextFile(UUID ownerId, String directoryName, String fileName, FilePrivacy privacyLevel, FileLifeSpan lifeSpan) throws FileNotFoundException,CantCreateFileException {
        checkContext();
        try {
            //execute AndroidPluginTextFile constructor
            AndroidPluginTextFile newFile = new AndroidPluginTextFile(ownerId, this.context,directoryName, hashFileName(fileName), privacyLevel, lifeSpan);
            //load content file
            newFile.loadFromMedia();
            return newFile;
        }
        catch (CantLoadFileException e){
            throw new FileNotFoundException(FileNotFoundException.DEFAULT_MESSAGE, e, "", "Check the cause");
        }
    }

    /**
     *<p>This method create a new PluginTextFile object.
     *
     * @param ownerId PlugIn id
     * @param directoryName name of the directory where the files are stored
     * @param fileName name of file to manage
     * @param privacyLevel level of privacy for the file, if it is public or private
     * @param lifeSpan lifeSpan of the file, whether it is permanent or temporary
     * @return PluginTextFile object
     * @throws CantCreateFileException
     */
    @Override
    public PluginTextFile createTextFile(UUID ownerId, String directoryName, String fileName, FilePrivacy privacyLevel, FileLifeSpan lifeSpan) throws CantCreateFileException{
        checkContext();
        return new AndroidPluginTextFile(ownerId, this.context, directoryName, hashFileName(fileName), privacyLevel, lifeSpan);
    }

    /**
     *<p>This method return a PluginBinaryFile object and load file content on memory
     *
     * @param ownerId PlugIn id
     * @param directoryName name of the directory where the files are stored
     * @param fileName name of file to load
     * @param privacyLevel level of privacy for the file, if it is public or private
     * @param lifeSpan lifeSpan of the file, whether it is permanent or temporary
     * @return PluginBinaryFile object
     * @throws FileNotFoundException
     * @throws CantCreateFileException
     */
    @Override
    public PluginBinaryFile getBinaryFile(UUID ownerId, String directoryName, String fileName, FilePrivacy privacyLevel, FileLifeSpan lifeSpan) throws FileNotFoundException,CantCreateFileException{
        checkContext();
        try {
            AndroidPluginBinaryFile newFile = new AndroidPluginBinaryFile(ownerId, this.context, directoryName, hashFileName(fileName), privacyLevel, lifeSpan);
            newFile.loadFromMedia();
            return newFile;
        } catch (CantLoadFileException e){
            throw new FileNotFoundException(FileNotFoundException.DEFAULT_MESSAGE, e, "", "Check the cause");
        }
    }

    /**
     *<p>This method create a new PluginBinaryFile object.
     *
     * @param ownerId PlugIn id
     * @param directoryName name of the directory where the files are stored
     * @param fileName name of file to load
     * @param privacyLevel level of privacy for the file, if it is public or private
     * @param lifeSpan lifeSpan of the file, whether it is permanent or temporary
     * @return PluginBinaryFile object
     * @throws CantCreateFileException
     */

    @Override
    public PluginBinaryFile createBinaryFile(UUID ownerId, String directoryName, String fileName, FilePrivacy privacyLevel, FileLifeSpan lifeSpan) throws CantCreateFileException{
        checkContext();
        return new AndroidPluginBinaryFile(ownerId, this.context, directoryName,hashFileName(fileName), privacyLevel, lifeSpan);
    }

    /**
     *<p>This method set the os context
     *
     * @param context Android context object
     */
    @Override
    public void setContext(Object context) {
        this.context = (Context) context;
    }

    /**
     *
     * Hash the file name using the algorithm SHA 256
     */

    private String hashFileName(String fileName) throws CantCreateFileException{
        try {
            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md.update(fileName.getBytes(Charset.forName(CHARSET_NAME)));
            byte[] digest = md.digest();
            byte[] encoded = Base64.encode(digest, 1);
            String encryptedString = new String(encoded, CHARSET_NAME);
            encryptedString = encryptedString.replace("/","");
            return encryptedString.replace("\n","");
        } catch (Exception e) {
            throw new CantCreateFileException(CantCreateFileException.DEFAULT_MESSAGE, FermatException.wrapException(e), "", "This Should never happen unless we change the DIGEST_ALGORITHM Constant");
        }

    }

    private void checkContext() throws CantCreateFileException {
        if(context == null)
            throw new CantCreateFileException(CantCreateFileException.DEFAULT_MESSAGE, null, "Context: null", "Context can't ne Null, you need to call setContext before using the FileSystem");
    }
}
