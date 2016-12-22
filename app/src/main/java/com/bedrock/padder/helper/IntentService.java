package com.bedrock.padder.helper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bedrock.padder.BuildConfig;
import com.bedrock.padder.R;

@SuppressWarnings("unchecked")

public class IntentService {
    public void intent(final Activity activity, String name, int delay) {
        final String classname = "com.bedrock.padder." + name;
        final Class<Object> classToLoad;
        try{
            classToLoad = (Class<Object>)Class.forName(classname);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("IntentService", "intent");
                    Intent animActivity = new Intent(activity, classToLoad);
                    activity.startActivity(animActivity);

                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }, delay);
        } catch (ClassNotFoundException e){
            Log.i("IntentService", "Error, there is no such class");
        }
    }

    public void intentSharedElement(final Activity activity, final String target_name, final int start_element_id, final String transition_name, int delay) {
        final String classname = "com.bedrock.padder." + target_name;
        final Class<Object> classToLoad;
        try{
            classToLoad = (Class<Object>)Class.forName(classname);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(Build.VERSION.SDK_INT >= 21) {
                        Intent intent = new Intent(activity, classToLoad);
                        View view = (View) activity.findViewById(start_element_id);

                        ActivityOptionsCompat options =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                                        view,           // The view which starts the transition
                                        transition_name // The transitionName of the view we’re transitioning to
                                );
                        ActivityCompat.startActivity(activity, intent, options.toBundle());
                    } else {
                        intent(activity, target_name, 0);
                    }
                }
            }, delay);
        } catch (ClassNotFoundException e){
            Log.i("IntentService", "Error, there is no such class");
        }
    }

    public void intentSharedElementWithExtra(final Activity activity, final String target_name, final int start_element_id, final String transition_name, final String extra_name, final int extra, int delay) {
        final String classname = "com.bedrock.padder." + target_name;
        final Class<Object> classToLoad;
        try{
            classToLoad = (Class<Object>)Class.forName(classname);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(Build.VERSION.SDK_INT >= 21) {
                        Intent intent = new Intent(activity, classToLoad);
                        intent.putExtra(extra_name, extra);
                        View view = (View) activity.findViewById(start_element_id);

                        ActivityOptionsCompat options =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                                        view,           // The view which starts the transition
                                        transition_name // The transitionName of the view we’re transitioning to
                                );
                        ActivityCompat.startActivity(activity, intent, options.toBundle());
                    } else {
                        intent(activity, target_name, 0);
                    }
                }
            }, delay);
        } catch (ClassNotFoundException e){
            Log.i("IntentService", "Error, there is no such class");
        }
    }

    public void intentSharedElementWithExtra(final Activity activity, final String target_name, final int start_element_id, final String transition_name, final String extra_name, final String extra, int delay) {
        final String classname = "com.bedrock.padder." + target_name;
        final Class<Object> classToLoad;
        try{
            classToLoad = (Class<Object>)Class.forName(classname);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(Build.VERSION.SDK_INT >= 21) {
                        Intent intent = new Intent(activity, classToLoad);
                        intent.putExtra(extra_name, extra);
                        View view = (View) activity.findViewById(start_element_id);

                        ActivityOptionsCompat options =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                                        view,           // The view which starts the transition
                                        transition_name // The transitionName of the view we’re transitioning to
                                );
                        ActivityCompat.startActivity(activity, intent, options.toBundle());
                    } else {
                        intent(activity, target_name, 0);
                    }
                }
            }, delay);
        } catch (ClassNotFoundException e){
            Log.i("IntentService", "Error, there is no such class");
        }
    }

    public void intentFlag(final Activity activity, String name, int delay) {
        final String classname = "com.bedrock.padder." + name;
        final Class<Object> classToLoad;
        try{
            classToLoad = (Class<Object>)Class.forName(classname);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("IntentService", "intentFlag");
                    Intent animActivity = new Intent(activity, classToLoad);
                    animActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    animActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(animActivity);

                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }, delay);
        } catch (ClassNotFoundException e){
            Log.i("IntentService", "Error, there is no such class");
        }
    }

    public void intentFinish(final Activity activity, int delay){
        Handler finish = new Handler();
        finish.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("IntentService", "intentFinish");
                activity.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                activity.finish();
            }
        }, delay);
    }

    public void intentLink(final Activity activity, String link, int delay) {
        final String url[] = {link};
        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            url[0] = "http://" + link;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url[0]));
                activity.startActivity(browserIntent);

                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, delay);
    }

    public void intentShareText(final Activity activity, String subject, String text, final String hint, int delay) {
        final Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, subject);
        share.putExtra(Intent.EXTRA_TEXT, text);

        Handler shareDelay = new Handler();
        shareDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.startActivity(Intent.createChooser(share, hint));
            }
        }, delay);
    }

    public void intentMarket(final Activity activity, final String appPackage, int delay) {
        Uri uri = Uri.parse("market://details?id=" + appPackage);
        final Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        if(Build.VERSION.SDK_INT >= 21) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        } else {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }

        Handler marketDelay = new Handler();
        marketDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    activity.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Log.d("ActivityNotFoundExp", "Activity not found, opening in browser");
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackage)));
                }
            }
        }, delay);
    }

    public void intentEmail(final Activity activity, String email_target, String subject, String subject_desc, String text, String text_desc, final String hint, int delay) {
        String br = System.getProperty("line.separator");
        String info;
        if(text_desc == null || text_desc.equals("")) {
            info = br + br + br + br + "Client : Android [" + BuildConfig.VERSION_NAME + "]";
        } else {
            info = br + br + br + br + "Link : " + text_desc + br + br + "Client : Android [" + BuildConfig.VERSION_NAME + "]";
        }

        final Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL  , new String[]{email_target});
        email.putExtra(Intent.EXTRA_SUBJECT, subject_desc + " : " + subject);
        email.putExtra(Intent.EXTRA_TEXT   , text + info);

        Handler emailDelay = new Handler();
        emailDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    activity.startActivity(Intent.createChooser(email, hint));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
                }
            }
        }, delay);
    }
}
