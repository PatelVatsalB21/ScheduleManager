package com.example.schedulemanager.LoginandUser;import android.content.Context;import android.content.Intent;import android.net.ConnectivityManager;import android.os.Bundle;import android.os.Handler;import android.view.WindowManager;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.widget.ImageView;import android.widget.TextView;import androidx.annotation.Nullable;import androidx.appcompat.app.AppCompatActivity;import com.example.schedulemanager.MainActivity;import com.example.schedulemanager.R;import com.example.schedulemanager.Setting.Settings_Main;import com.google.android.gms.ads.AdListener;import com.google.android.gms.ads.AdRequest;import com.google.android.gms.ads.InterstitialAd;import com.google.android.gms.ads.LoadAdError;import com.google.android.gms.ads.MobileAds;import java.util.ArrayList;import java.util.Arrays;import java.util.List;import java.util.Random;public class SplashScreen extends AppCompatActivity {    ImageView imageView;    TextView txt, quote_txt_view;    InterstitialAd mInterstitialAd;    @Override    protected void onCreate(@Nullable Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.splash_screen);        MobileAds.initialize(this, initializationStatus -> {        });        Settings_Main.LoadChangedSettings(SplashScreen.this);        mInterstitialAd = new InterstitialAd(this);        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");        mInterstitialAd.loadAd(new AdRequest.Builder().build());        txt = findViewById(R.id.splashtxt);        imageView = findViewById(R.id.splashicon);        quote_txt_view = findViewById(R.id.SplashQuoteTxtView);        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,                WindowManager.LayoutParams.FLAG_FULLSCREEN);        Animation splashAnim = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fade_in);        txt.startAnimation(splashAnim);        quote_txt_view.setText(randomQuoteSelector());        imageView.startAnimation(splashAnim);        new Handler().postDelayed(() -> {            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(                    Context.CONNECTIVITY_SERVICE);            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()                    && conMgr.getActiveNetworkInfo().isConnected()) {                if (mInterstitialAd.isLoaded()) {                    mInterstitialAd.show();                    mInterstitialAd.setAdListener(new AdListener() {                        @Override                        public void onAdLoaded() {                        }                        @Override                        public void onAdFailedToLoad(LoadAdError adError) {                            startActivity(new Intent(SplashScreen.this, MainActivity.class));                            finish();                        }                        @Override                        public void onAdOpened() {                        }                        @Override                        public void onAdClicked() {                        }                        @Override                        public void onAdLeftApplication() {                            finish();                        }                        @Override                        public void onAdClosed() {                            startActivity(new Intent(SplashScreen.this, MainActivity.class));                            finish();                        }                    });                } else {                    startActivity(new Intent(SplashScreen.this, MainActivity.class));                    finish();                }            } else {                startActivity(new Intent(SplashScreen.this, MainActivity.class));                finish();            }        }, 4500);    }    List<String> quotes = new ArrayList<String>(            Arrays.asList("It doesn’t matter how Slowly you go as Long as you do not Stop",                    "“Mistakes are Proof that you are Trying”",                    "“Just it’s Hard doesn’t mean it’s Impossible”",                    "“Be a Mermaid and make Waves”",                    "“The Expert in Anything was once a Beginner”",                    "“Life begins at the End of your Comfort Zone”",                    "“If you can Dream it, you can Do It”",                    "“If Opportunity doesn’t Knock, Build a door”",                    "“Your Limitation – It’s only your Imagination”",                    "“You are the only one who can limit your Greatness”",                    "“Aim for the Moon. If you miss, you may hit a Star”",                    "“Do or do not. There is no Try”",                    "“Success is walking from Failure to Failure with no loss of Enthusiasm”",                    "“The Harder the Conflict, the more Glorious the Triumph”",                    "“Sometimes later becomes Never, Do it now”",                    "“Be Yourself, Everyone else is already taken”",                    "“Your future is created by what you do Today, not Tomorrow”",                    "“Success is how High you Bounce when you hit the Bottom”",                    "“Stay Humble, Work Hard, Be Kind”",                    "“The Best way to predict Future is to Create it”",                    "“No one Rises suddenly in the World, not even the Sun”",                    "“Don’t be Afraid to do Something Big”",                    "“Let Today be the Start of Something New”",                    "“Today is a New Beginning”",                    "“Focus on the Future not your Past”",                    "“Be the change you Wish to see in the World”",                    "“If it doesn’t Challenge you, it won’t Change you”",                    "“Better is not something you Wish. It’s something you Become”",                    "“Make Your Dreams Happen”",                    "“Failure is the Opportunity to begin again more Intelligently”",                    "“There is nothing Impossible to him who will Try”",                    "“Knowledge is borrowed, Wisdom is unique”",                    "“In the End, we only Regret the Chances we didn’t Take”",                    "“No matter how Hard the Past was, you can always Begin again”",                    "“Who you are Tomorrow begins with what you do Today”",                    "“Don’t watch the Clock, do what is does. Keep Going”",                    "“People with great Passion can make the Impossible happen”",                    "“Do it with Passion or Not At All”",                    "“Follow your Passion; it will lead you to your Purpose”",                    "“Don’t be afraid to Fail. Be afraid Not To Try”",                    "“A person who never made a Mistake, never Tried anything new”",                    "“Stars can’t shine without Darkness”",                    "“Trust that an Ending is followed by the Beginning”",                    "“There is No Elevator to the Success. You have to take the Stairs”",                    "“Push yourself because, no one else is going to do it for you”",                    "“Push harder than Yesterday if you want a different Tomorrow”",                    "“The Cave you fear to Enter, holds the Treasure you seek”",                    "“The Limits we set becomes the Prison we live in”",                    "“Tough Times never last, but Tough people does”",                    "“Those who dare to fail Miserably can achieve Greatly”",                    "“Don’t forget to Smile today”",                    "“If your Dreams don’t scare you. They are too small”",                    "“Making Mistakes is Better than Faking Perfections”",                    "“You didn’t come this far to only come This Far”",                    "“Difficulty is the excuse History Never Accepts”",                    "“A Smooth sea never made a Skilled Sailor”",                    "“Always believe something wonderful is about to happen”",                    "“You are stronger than you know”",                    "“Believing in your self is the key to Success”",                    "“Trust the Timing of your Life”",                    "“When nothing goes right, go Left”",                    "“No One Is You and that is your Power”",                    "“You are the Master of your Fate, You are the Captain of your Soul”",                    "“Your goals don’t care, How you Feel”",                    "“Never let your Fear decide your Future”",                    "“Whatever you are, be a good one”",                    "“It’s Never too Late”",                    "“Never forget why you started”",                    "“Don’t wish for work, Work For It”",                    "“Success is a Slow Process, but Quitting won’t speed it up”",                    "“You are always One Decision away from Totally Different Life”",                    "“The Body achieves what the Mind Believes”",                    "“Old Ways won’t open New Doors”",                    "“Follow Your Dreams”",                    "“Don’t tell people your Dreams. Show Them”",                    "“If it was Easy, Everyone would Do It”",                    "“If you want to fly, give up everything that Weighs You Down”",                    "“Success is a journey not a destination”",                    "“Do something today that your Future Self will Thank You for”",                    "“Do it now. Sometimes ‘Later’ becomes ‘Never’ ”",                    "“Believe you can & you are halfway there”",                    "“Dreams don’t work, Unless You Do”",                    "“Work until your idols becomes your rivals”",                    "“Hard work Pays Off”",                    "“Failure is Success in Progress”",                    "“It always seems impossible until it’s done”",                    "“The Comeback is always Stronger than the Setback”",                    "“Difficult roads often lead to Beautiful Destinations”",                    "“A Little Progress each day adds up to Big Results”",                    "“The true Entrepreneur is Doer, not Dreamer”",                    "“The Road to Success is always under Construction”",                    "“If you Can’t Stop Thinking about it, Don’t Stop Working for it ”",                    "“If you get Tired, learn to Rest, not to Quit”",                    "“Don’t Stop when you are Tired. Stop when you are Done”",                    "“The Price of Greatness is Responsibility”",                    "“The key to Success is to Start Before you are Ready”",                    "“You are Your only Limit”",                    "“Work hard in Silence, Let your success be the Noise”",                    "“It’s hard to Beat a person Who Never Gives Up”",                    "“Don’t wait for Opportunity. Create it”",                    "“Work Smart Work Hard”",                    "“Fortune favors the Brave”",                    "“Hard work beats Talent”",                    "“The journey of 1000 miles Begins with a Single Step”",                    "“When you feel like Quitting: Remember why you Started”",                    "“The Best View comes after the Hardest Climb”",                    "“Winners are not People who never Fail but people who never Quit”",                    "“Think positive & Positive things will Happen”",                    "“Failure is not the opposite of Success, it’s part of Success”",                    "“Work Hard Dream Big”",                    "“Everything is Hard, Before it is Easy”",                    "“When you can’t find Sunshine, Be the Sunshine”",                    "“You get what you Focus on”",                    "“Your Mistakes Don’t Define You”",                    "“Success is where Preparation and Opportunity meet”",                    "“Today is Chance to get Better”",                    "“Work until Expensive becomes Cheap”",                    "“Hard work never brings Fatigue, it brings Satisfaction”",                    "“Think Positive Talk Positive Feel Positive”",                    "“Never Give Up because Great things Take Time”",                    "“Always Believe in Yourself”",                    "“Progress is progress no matter how Small it is”",                    "“Don't Give up, Beginning is always the Hardest”",                    "“Intelligence without Ambition is a bird with wings”",                    "“When people throw Stones at you, convert them into Milestones”"));    public String randomQuoteSelector() {        Random random = new Random();        return quotes.get(random.nextInt(quotes.size()));    }}