package com.example.helloandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    Button bChrome;
    Button bBrowser;
    Button bSend;
    MyEditText eMessage;
    Button bPhone;
    public static final String key = "KEY_NAME";
    private static final int SHOW_OTHER_ACTIVITY = 1;
    private RetainedFragment mRetainedFragment;
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    public static TimeZone oldTZ;
    public static Locale locale;
    public ImageView frameImage;
    public AnimationDrawable frameAnimation;

    public ImageView tweenImage;
    public Animation tweenAnimation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(),"onCreate() called",Toast.LENGTH_LONG).show();

        //I haven't any large objects in program so will use Retained
        // Fragment for transferring entered text in Main Activity
        // when screen orientation or locale changes
        FragmentManager fm = getFragmentManager();
        mRetainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (mRetainedFragment == null) {
            mRetainedFragment = new RetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
        }
        // the data is available in mRetainedFragment.getData() even after
        // subsequent configuration change restarts.

        oldTZ = TimeZone.getDefault();
        locale = Locale.getDefault();

        executeFrameAnimation();
        executeTweenAnimation();
        setButtonListeners();
        refreshTextField();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape orientation", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait orientation", Toast.LENGTH_SHORT).show();
        }
        // this function is very similar to onCreate() contents, but calls
        // onRestart instead of onDestroy and onCreate
        setContentView(R.layout.activity_main);
        executeFrameAnimation();
        executeTweenAnimation();
        setButtonListeners();
        refreshTextField();
        eMessage.setText(mRetainedFragment.getData());

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "hard keyboard is exposed", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "hard keyboard is hidden", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_UNDEFINED) {
            Toast.makeText(this, "hard keyboard is undef", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (SHOW_OTHER_ACTIVITY):
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra(OtherActivity.resultKey);
                    Toast.makeText(getApplicationContext(),"Received: " + result,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


        @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"onStart() called",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(),"onRestart() called",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"onResume() called",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(),"onPause() called",Toast.LENGTH_LONG).show();

        if(isFinishing()) {
            FragmentManager fm = getFragmentManager();
            // we will not need this fragment anymore, this may also be a good place to signal
            // to the retained fragment object to perform its own cleanup.
            Toast.makeText(getApplicationContext(),"Retained fragment no more contains " + oldTZ.getID() + "and will be destroyed",Toast.LENGTH_LONG).show();
            fm.beginTransaction().remove(mRetainedFragment).commit();
        }

        eMessage.clearFocus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(),"onStop() called",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"onDestroy() called",Toast.LENGTH_LONG).show();
    }

    protected void executeTweenAnimation() {
        tweenImage = findViewById(R.id.secImageView);
        tweenAnimation = AnimationUtils.loadAnimation(this, R.anim.tween);
        tweenAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tweenImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        tweenImage.startAnimation(tweenAnimation);
    }

    protected void executeFrameAnimation() {
        frameImage = (ImageView) findViewById(R.id.imageView);
        frameImage.setBackgroundResource(R.drawable.frame);
        frameAnimation = (AnimationDrawable) frameImage.getBackground();
        frameAnimation.start();
    }

    protected void setButtonListeners() {
        bBrowser = findViewById(R.id.bBrowser);
        bChrome = findViewById(R.id.bChrome);
        bSend = findViewById(R.id.bSend);
        bPhone = findViewById(R.id.bPhone);

        bChrome.setOnClickListener(view -> {
            PackageManager manager = getPackageManager();
            Intent chromeIntent = manager.getLaunchIntentForPackage("com.android.chrome");

            if (chromeIntent != null) {
                chromeIntent.setData(Uri.parse("https://www.google.com/search?q=" + eMessage.getText().toString()));
                eMessage.clearFocus();
                startActivity(chromeIntent);
            } else {
                Toast.makeText(getApplicationContext(),"no Chrome application",Toast.LENGTH_LONG).show();
            }
        });
        bBrowser.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://www.google.com/search?q=" + eMessage.getText().toString()));
            browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(browserIntent);
            } else {
                Toast.makeText(getApplicationContext(),"no suitable browser application",Toast.LENGTH_LONG).show();
            }
        });
        bPhone.setOnClickListener(view -> {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            String data = eMessage.getText().toString();
            phoneIntent.setData(Uri.parse("tel:" + data));
            if (phoneIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(phoneIntent);
            } else {
                Toast.makeText(getApplicationContext(),"no suitable phone application",Toast.LENGTH_LONG).show();
            }
        });
        bSend.setOnClickListener(view -> {
            Intent messageIntent = new Intent(getApplicationContext(), OtherActivity.class);
            messageIntent.putExtra(key, eMessage.getText().toString());
            startActivityForResult(messageIntent, 1);
        });
    }

    protected void refreshTextField() {
        eMessage = findViewById(R.id.eMessage);
        eMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // Main function to control the focus over the soft keyboard on calling view
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Toast.makeText(getApplicationContext(),"Keyboard is exposed",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Keyboard is hidden",Toast.LENGTH_LONG).show();
                    mRetainedFragment.setData(eMessage.getText().toString());
                    Toast.makeText(getApplicationContext(),"Retained fragment now contains message: " + mRetainedFragment.getData(),Toast.LENGTH_LONG).show();
                }
            }
        });
        eMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // Catching Done button pressed on the soft keyboard
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                final boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    // обрабатываем нажатие кнопки
                    eMessage.clearFocus();
                }
                return handled;
            }
        });
    }

}