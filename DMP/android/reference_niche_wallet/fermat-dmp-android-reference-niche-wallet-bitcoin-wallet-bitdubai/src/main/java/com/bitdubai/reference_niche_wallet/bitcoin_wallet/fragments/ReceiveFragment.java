package com.bitdubai.reference_niche_wallet.bitcoin_wallet.fragments;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_fermat_dmp_wallet_bitcoin.R;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.PlatformWalletType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.enums.Wallets;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetCryptoWalletException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantRequestCryptoAddressException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.interfaces.CryptoWallet;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.interfaces.CryptoWalletManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedWalletExceptionSeverity;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.Platform;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Natalia on 02/06/2015.
 */
public class ReceiveFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    View rootView;

    private String user_address_wallet = "";
    final int colorQR = Color.BLACK;
    final int colorBackQR = Color.WHITE;
    final int width = 400;
    final int height = 400;
    UUID wallet_id = UUID.fromString("25428311-deb3-4064-93b2-69093e859871");

    /**
     * DealsWithNicheWalletTypeCryptoWallet Interface member variables.
     */
    private static CryptoWalletManager cryptoWalletManager;
    private static Platform platform = new Platform();
    private CryptoWallet cryptoWallet;
    private ErrorManager errorManager;


    public static ReceiveFragment newInstance(int position) {
        ReceiveFragment f = new ReceiveFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    public void onCreate(Bundle savedInstanceState) {
        //setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
        try {
            errorManager = platform.getErrorManager();
            cryptoWalletManager = platform.getCryptoWalletManager();
            try
            {
                cryptoWallet = cryptoWalletManager.getCryptoWallet();
            }
            catch (CantGetCryptoWalletException e)
            {
                errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                showMessage("Unexpected error init Crypto Manager - " + e.getMessage());
            }
        } catch (Exception ex) {
            showMessage("Unexpected error init Crypto Manager - " + ex.getMessage());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wallets_bitcoin_fragment_receive, container, false);

        final EditText contact_name = (EditText) rootView.findViewById(R.id.contact_name);
        contact_name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                resetForm();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // request_address button definition
        final Button requestAdressBtn = (Button) rootView.findViewById(R.id.request_btn);
        requestAdressBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestAddress();
            }
        });

        // share_address button definition
        final Button shareAddressBtn = (Button) rootView.findViewById(R.id.share_btn);
        shareAddressBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareAddress();
            }
        });

        // share_address button definition
        final TextView stringAddressTextView = (TextView) rootView.findViewById(R.id.string_address);
        stringAddressTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyToClipboard();
            }
        });
        return rootView;
    }

    public void resetForm() {
        ImageView imageQR = (ImageView) rootView.findViewById(R.id.qr_code);
        imageQR.setVisibility(View.GONE);
        TextView string_address = (TextView) rootView.findViewById(R.id.string_address);
        string_address.setVisibility(View.GONE);
        Button share_btn = (Button) rootView.findViewById(R.id.share_btn);
        share_btn.setVisibility(View.GONE);
    }

    public void requestAddress() {
        final EditText nameText = (EditText)rootView.findViewById(R.id.contact_name);

        if (nameText != null && nameText.getText().toString() != null && !nameText.getText().toString().equals("")) {
            getWalletAddress(nameText.getText().toString());
            showQRCodeAndAddress();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Enter a name to share your address", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareAddress(){
        Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, user_address_wallet );
        startActivity(Intent.createChooser(intent2, "Share via"));
    }

    private void showQRCodeAndAddress() {
        try {
            // get qr image
            Bitmap bitmapQR = generateBitmap(user_address_wallet, width, height, MARGIN_AUTOMATIC, colorQR, colorBackQR);
            // set qr image
            ImageView imageQR = (ImageView) rootView.findViewById(R.id.qr_code);
            imageQR.setImageBitmap(bitmapQR);
            // show qr image
            imageQR.setVisibility(View.VISIBLE);

            // set string_address
            TextView string_address = (TextView) rootView.findViewById(R.id.string_address);
            string_address.setText(user_address_wallet);
            // show string_address
            string_address.setVisibility(View.VISIBLE);

            // show share_btn
            Button share_btn = (Button) rootView.findViewById(R.id.share_btn);
            share_btn.setVisibility(View.VISIBLE);

            //TODO SHOW BUTTON SHARE AND ADDRESS IN SCREEN
        } catch (WriterException writerException) {
            errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, writerException);

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getWalletAddress(String contact_name) {
        try {
            CryptoAddress cryptoAddress = cryptoWallet.requestAddress(contact_name.toString(), Actors.EXTRA_USER, PlatformWalletType.BASIC_WALLET_BITCOIN_WALLET, wallet_id);
            user_address_wallet = cryptoAddress.getAddress();
        }
        catch (CantRequestCryptoAddressException e)
        {
            errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage("Cant Request Crypto Address Exception - " + e.getMessage());

        }
    }

    /**
     * copy wallet address to clipboard
     */

    public void copyToClipboard() {

        ClipboardManager myClipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", this.user_address_wallet);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getActivity().getApplicationContext(), "Text Copied",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Allow the zxing engine use the default argument for the margin variable
     */
    static public int MARGIN_AUTOMATIC = -1;

    /**
     * Set no margin to be added to the QR code by the zxing engine
     */
    static public int MARGIN_NONE = 0;

    /**
     * Encode a string into a QR Code and return a bitmap image of the QR code
     *
     * @param contentsToEncode String to be encoded, this will often be a URL, but could be any string
     * @param imageWidth number of pixels in width for the resultant image
     * @param imageHeight number of pixels in height for the resultant image
     * @param marginSize the EncodeHintType.MARGIN parameter into zxing engine
     * @param color data color for QR code
     * @param colorBack background color for QR code
     * @return bitmap containing QR code image
     * @throws com.google.zxing.WriterException zxing engine is unable to create QR code data
     * @throws IllegalStateException when executed on the UI thread
     */
    static public Bitmap generateBitmap(@NonNull String contentsToEncode,
                                        int imageWidth, int imageHeight,
                                        int marginSize, int color, int colorBack)
            throws WriterException, IllegalStateException {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            // throw new IllegalStateException("Should not be invoked from the UI thread");
        }

        Map<EncodeHintType, Object> hints = null;
        if (marginSize != MARGIN_AUTOMATIC) {
            hints = new EnumMap<>(EncodeHintType.class);
            // We want to generate with a custom margin size
            hints.put(EncodeHintType.MARGIN, marginSize);
        }

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contentsToEncode, BarcodeFormat.QR_CODE, imageWidth, imageHeight, hints);

        final int width = result.getWidth();
        final int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? color : colorBack;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    //show alert
    private void showMessage(String text){
        AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage(text);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // aquí puedes añadir funciones
            }
        });
        //alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }
}