package com.ibphub.vcci_new.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.adapter.CategoriesAdapter;
import com.ibphub.vcci_new.adapter.popup_menu.PopupMenuAdapter;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.app_update.WSCallerVersionListener;
import com.ibphub.vcci_new.dialog.NewVersionAvailableDialog;
import com.ibphub.vcci_new.fragment.AboutUsFragment;
import com.ibphub.vcci_new.fragment.CmMessageFragment;
import com.ibphub.vcci_new.fragment.CommitteeFragment;
import com.ibphub.vcci_new.fragment.ContactFragment;
import com.ibphub.vcci_new.fragment.GalleryFragment;
import com.ibphub.vcci_new.fragment.HomeFragment;
import com.ibphub.vcci_new.fragment.LoginFragment;
import com.ibphub.vcci_new.fragment.MembershipFragment;
import com.ibphub.vcci_new.fragment.SettingsFragment;
import com.ibphub.vcci_new.fragment.events_fragment.EventsFragment;
import com.ibphub.vcci_new.interfaces.CategorySelectionListener;
import com.ibphub.vcci_new.interfaces.HideShowHomeLogoInterface;
import com.ibphub.vcci_new.interfaces.HideShowIconInterface;
import com.ibphub.vcci_new.interfaces.HideShowPopUpMenuIconInterface;
import com.ibphub.vcci_new.interfaces.LanguageSelectionListener;
import com.ibphub.vcci_new.interfaces.OtherMenuSelectionListener;
import com.ibphub.vcci_new.interfaces.UpdateDialogClickListener;
import com.ibphub.vcci_new.model.category.CategoriesItem;
import com.ibphub.vcci_new.model.category.CategoriesResponse;
import com.ibphub.vcci_new.util.LocaleManager;
import com.ibphub.vcci_new.util.Stash;

import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ibphub.vcci_new.config.Configuration.DEVICE_TYPE;

public class MainActivityWithLogin extends BaseActivity implements HideShowIconInterface, CategorySelectionListener,
        OtherMenuSelectionListener, HideShowPopUpMenuIconInterface, HideShowHomeLogoInterface, LanguageSelectionListener,
        WSCallerVersionListener, UpdateDialogClickListener {

    private static final String TAG = MainActivityWithLogin.class.getSimpleName();
    private NavigationView navigationView;

    private DrawerLayout drawer;
    private View navHeader;
    private AppCompatImageView imgNavHeaderBg, imgProfile;
    private AppCompatTextView tv_welcome, tv_gcci;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ImageView iv_category;
    private ImageView iv_category_close;
    private ImageView iv_home_logo;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_ABOUT_US = "about_us";
    private static final String TAG_COMMITEE = "commitee";
    private static final String TAG_CM_MESSAGE = "cm_message";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_MEMBERSHIP = "membership";
    private static final String TAG_GALLERY = "gallery";
    private static final String TAG_LIBRARY = "library";
    private static final String TAG_CONTACT = "contact";
    public static final String TAG_LOGIN = "login";
    public static final String TAG_LOGOUT = "logout";
    public static final String TAG_SETTING = "setting";
    public static final String TAG_CIRCULAR = "circular";
    public static String CURRENT_TAG = TAG_HOME;

    private static final String POPUP_CONSTANT = "mPopup";
    private static final String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private final boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private CategoriesAdapter categoriesAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private LinearLayout ll_close_category;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView toolbar_title;

    // No Internet Dialog
    NoInternetDialog noInternetDialog;

    // No Internet Snackbar
    NoInternetSnackbar noInternetSnackbar;

    private TextView tv_version;

    // social links
    private ImageView iv_fblogo;
    private ImageView iv_twitterlogo;
    private ImageView iv_linkedinlogo;
    private ImageView iv_youtube;

    private AppUpdateManager appUpdateManager;

    int commiteeID = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setNewLocale(this, LocaleManager.GUJARATI);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        // new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();

        // Fetch Firebase Token
        //cY4ghoGDFhw:APA91bHn-cZOMN3MYpIFyYIqzDhMbMshpzind_eGVeotHSGmL69hQUAG276kDH7VWR4Ivk0T9k1_nDnNYUG2eBigD_vCqezMZ56zVpqa3sVN0TcDEJa9siulqSF4IbzrHJx9E0L-PWuZ

        /*FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    Log.d(TAG, token);
                    Log.d(TAG, "onCreate: " + token);
                    Stash.put("fcm_token", token);
                    //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                });*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        mHandler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //fab = findViewById(R.id.fab);

        drawer.setScrimColor(Color.TRANSPARENT);

        tv_version = findViewById(R.id.tv_version);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tv_version.setText(String.format("%s - %s", getResources().getString(R.string.version), version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        iv_fblogo = findViewById(R.id.iv_fblogo);
        iv_linkedinlogo = findViewById(R.id.iv_linkedinlogo);
        iv_twitterlogo = findViewById(R.id.iv_twitterlogo);
        iv_youtube = findViewById(R.id.iv_youtube);

        iv_fblogo.setOnClickListener(v -> openSocialLink("Facebook", "https://www.facebook.com/GCCIAhmedabad/"));
        iv_linkedinlogo.setOnClickListener(v -> openSocialLink("LinkedIn", "https://www.linkedin.com/company/gcciahmedabad/"));
        iv_twitterlogo.setOnClickListener(v -> openSocialLink("Twitter", "https://twitter.com/GCCIAhmedabad"));
        iv_youtube.setOnClickListener(v -> openSocialLink("Youtube Channel", "https://www.youtube.com/channel/UCGAAAgyTUtpCPv6aOk2mTWQ?view_as=subscriber"));

        iv_home_logo = findViewById(R.id.iv_home_logo);
        iv_category = findViewById(R.id.iv_category);
        iv_category_close = findViewById(R.id.iv_category_close);

        recyclerView = findViewById(R.id.lv_category);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ll_close_category = findViewById(R.id.ll_close_category);

        ll_close_category.setOnClickListener(v -> {
            recyclerView.setVisibility(View.GONE);
            ll_close_category.setVisibility(View.GONE);
            iv_category_close.setVisibility(View.GONE);
            iv_category.setVisibility(View.VISIBLE);

        });

        iv_category_close.setOnClickListener(v -> {
            iv_category.setVisibility(View.VISIBLE);
            iv_category_close.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            ll_close_category.setVisibility(View.GONE);
        });

        iv_category.setOnClickListener(v -> {
            iv_category.setVisibility(View.GONE);
            iv_category_close.setVisibility(View.VISIBLE);
            getSliderMenu();
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawers();
                recyclerView.setVisibility(View.VISIBLE);
                ll_close_category.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                ll_close_category.setVisibility(View.VISIBLE);
            }
        });

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        tv_welcome = navHeader.findViewById(R.id.tv_welcome);
        tv_gcci = navHeader.findViewById(R.id.tv_gcci);
        //imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void openSocialLink(String social_site_name, String social_url) {
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(social_url));

// Always use string resources for UI text. This says something like "Share this photo with"
        String title = getResources().getString(R.string.chooser_title);
// Create and start the chooser
        Intent chooser = Intent.createChooser(intent, title);
        startActivity(chooser);*/

        Intent toDetails = new Intent(MainActivityWithLogin.this, SocialSitesActivity.class);
        toDetails.putExtra("social_site_name", social_site_name);
        toDetails.putExtra("social_url", social_url);
        startActivity(toDetails);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // No Internet Snackbar
        NoInternetSnackbar.Builder builder2 = new NoInternetSnackbar.Builder(this, (ViewGroup) findViewById(android.R.id.content));

        // Optional
        builder2.setConnectionCallback(hasActiveConnection -> {
            // ...
            // Stuff that updates the UI
            runOnUiThread(this::getHomeFragment);
        });
        builder2.setIndefinite(true); // Optional
        builder2.setNoInternetConnectionMessage("No active Internet connection!"); // Optional
        builder2.setOnAirplaneModeMessage("You have turned on the airplane mode!"); // Optional
        builder2.setSnackbarActionText("Settings");
        builder2.setShowActionToDismiss(false);
        builder2.setSnackbarDismissActionText("OK");

        noInternetSnackbar = builder2.build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // No Internet Snackbar
        if (noInternetSnackbar != null) {
            noInternetSnackbar.destroy();
        }
    }

    public void getSliderMenu() {
        Log.d(TAG, "getSliderMenu: " + Stash.getString("menu_for"));
        if ("home".equals(Stash.getString("menu_for"))) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            String token = Stash.getString("fcm_token");
            Log.d(TAG, "getSliderMenu: " + token);
            Call<CategoriesResponse> call = apiService.getCategoriesList("1.0", DEVICE_TYPE, "get-categories",
                    "token", "123456", "1", "100");
            call.enqueue(new Callback<CategoriesResponse>() {
                @Override
                public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                    assert response.body() != null;
                    List<CategoriesItem> categoriesItemsWithHeader = new ArrayList<>();
                    CategoriesItem categoriesItem = new CategoriesItem();
                    categoriesItem.setId(-1);
                    categoriesItem.setTitle("");
                    categoriesItemsWithHeader.add(categoriesItem);
                    List<CategoriesItem> categoriesItemsList = response.body().getCategories();
                    categoriesItemsWithHeader.addAll(categoriesItemsList);
                    Log.d(TAG, "Categories received: " + categoriesItemsWithHeader.size());
                    categoriesAdapter = new CategoriesAdapter(MainActivityWithLogin.this, categoriesItemsWithHeader, MainActivityWithLogin.this);
                    recyclerView.setAdapter(categoriesAdapter);

                }

                @Override
                public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
        } else if ("events".equals(Stash.getString("menu_for"))) {
            List<String> event_menu = new ArrayList<>();
            event_menu.add(getResources().getString(R.string.upcoming_events));
            event_menu.add(getResources().getString(R.string.past_events));
            PopupMenuAdapter popupMenuAdapter = new PopupMenuAdapter(MainActivityWithLogin.this,
                    event_menu, MainActivityWithLogin.this);
            recyclerView.setAdapter(popupMenuAdapter);
        } else if ("gallery".equals(Stash.getString("menu_for"))) {
            List<String> gallery_menu = new ArrayList<>();
            gallery_menu.add(getResources().getString(R.string.photo_gallery));
            gallery_menu.add(getResources().getString(R.string.video_gallery));
            PopupMenuAdapter popupMenuAdapter = new PopupMenuAdapter(MainActivityWithLogin.this,
                    gallery_menu, MainActivityWithLogin.this);
            recyclerView.setAdapter(popupMenuAdapter);
        } else if ("committee".equals(Stash.getString("menu_for"))) {
            List<String> committee_menu = new ArrayList<>();
            committee_menu.add(getResources().getString(R.string.office_bearers));
            committee_menu.add(getResources().getString(R.string.working_committee_2));
            committee_menu.add(getResources().getString(R.string.co_opted_members));
            committee_menu.add(getResources().getString(R.string.invitee_members));
            committee_menu.add(getResources().getString(R.string.special_invitee_members));
            committee_menu.add(getResources().getString(R.string.advisory_board));
            committee_menu.add(getResources().getString(R.string.working_committee));
            //committee_menu.add(getResources().getString(R.string.managing_committee_member));


            PopupMenuAdapter popupMenuAdapter = new PopupMenuAdapter(MainActivityWithLogin.this,
                    committee_menu, MainActivityWithLogin.this);
            recyclerView.setAdapter(popupMenuAdapter);
        } else {
            List<String> about_us_menu = new ArrayList<>();
            about_us_menu.add(getResources().getString(R.string.profile));
            about_us_menu.add(getResources().getString(R.string.events_and_activities));
            about_us_menu.add(getResources().getString(R.string.future_plan));
            about_us_menu.add(getResources().getString(R.string.office_bearers));
            about_us_menu.add(getResources().getString(R.string.executive_committee));
            about_us_menu.add(getResources().getString(R.string.past_president));
            about_us_menu.add(getResources().getString(R.string.secretariat));
            PopupMenuAdapter popupMenuAdapter = new PopupMenuAdapter(MainActivityWithLogin.this,
                    about_us_menu, MainActivityWithLogin.this);
            recyclerView.setAdapter(popupMenuAdapter);
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */

    private void loadNavHeader() {
        Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(this).getAssets(), "fonts/OpenSans-Bold.ttf");
        tv_welcome.setText(R.string.welcome_to);
        tv_gcci.setText(R.string.gcci);
        tv_welcome.setTypeface(custom_font);
        tv_gcci.setTypeface(custom_font);
        // showing dot next to notifications label
        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = () -> {
            // update the main content by replacing fragments
            Fragment fragment = getHomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        if (Stash.getBoolean("isVerified")) {
            switch (navItemIndex) {
                case 0:
                    // home
                    return new HomeFragment();
                case 1:
                    // about us
                    return new AboutUsFragment(1, getResources().getString(R.string.profile));
                case 2:
                    // committee fragment
                    return new CommitteeFragment(7, getResources().getString(R.string.office_bearers));
                case 3:
                    // cm message fragment
                    return new CmMessageFragment();
                case 4:
                    // settings fragment
                    return new EventsFragment(1, getResources().getString(R.string.upcoming_events));
                case 5:
                    // settings fragment
                    return new MembershipFragment();
                case 6:
                    // Circulars fragment
                    return new MembershipFragment();
                case 7:
                    // gallery fragment
                    return new GalleryFragment(1, getResources().getString(R.string.photo_gallery));
            /*case 7:
                // settings fragment
                return new LibraryFragment();*/
                case 8:
                    // settings fragment
                    return new ContactFragment();
                case 9:
                    // logout fragment
                    //return new LoginFragment();
                case 10:
                    // settings fragment
                    return new SettingsFragment();
                default:
                    return new HomeFragment();
            }
        } else {
            switch (navItemIndex) {
                case 0:
                    // home
                    return new HomeFragment();
                case 1:
                    // about us
                    return new AboutUsFragment(1, getResources().getString(R.string.profile));
                case 2:
                    // committee fragment
                    return new CommitteeFragment(7, getResources().getString(R.string.office_bearers));
                case 3:
                    // cm message fragment
                    return new CmMessageFragment();
                case 4:
                    // settings fragment
                    return new EventsFragment(1, getResources().getString(R.string.upcoming_events));
                case 5:
                    // settings fragment
                    return new MembershipFragment();
                case 6:
                    // gallery fragment
                    return new GalleryFragment(1, getResources().getString(R.string.photo_gallery));
            /*case 7:
                // settings fragment
                return new LibraryFragment();*/
                case 7:
                    // settings fragment
                    return new ContactFragment();
                case 8:
                    // settings fragment
                    return new LoginFragment();
                case 9:
                    // settings fragment
                    return new SettingsFragment();
                default:
                    return new HomeFragment();
            }
        }
    }

    private void setToolbarTitle() {
        toolbar_title.setText(activityTitles[navItemIndex]);
        Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(this).getAssets(), "fonts/OpenSans-Bold.ttf");
        toolbar_title.setTypeface(custom_font);

        //getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

                    case R.id.nav_about_us:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ABOUT_US;
                        break;

                    case R.id.nav_commitee:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_COMMITEE;
                        break;
                    case R.id.nav_cm_message:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_CM_MESSAGE;
                        break;
                    case R.id.nav_event:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_EVENTS;
                        break;
                    case R.id.nav_membership:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_MEMBERSHIP;
                        break;
                    case R.id.nav_gallery:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_GALLERY;
                        break;
                    /*case R.id.nav_library:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_LIBRARY;
                        break;*/
                    case R.id.nav_contact:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_CONTACT;
                        break;
                    case R.id.nav_login:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_LOGIN;
                        break;
                    case R.id.nav_language:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_SETTING;
                        /*navItemIndex = navItemIndex;
                        CURRENT_TAG = CURRENT_TAG;
                        ChangeLanguageDialog changeLanguageDialog = new ChangeLanguageDialog(MainActivity.this, MainActivity.this);
                        changeLanguageDialog.show();*/
                        break;
                    default:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                menuItem.setChecked(!menuItem.isChecked());
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                    ll_close_category.setVisibility(View.GONE);
                    iv_category.setVisibility(View.VISIBLE);
                    iv_category_close.setVisibility(View.GONE);
                    super.onDrawerOpened(drawerView);
                } else {
                    super.onDrawerOpened(drawerView);
                }
            }
        };

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
                String news_type = Stash.getString("news_type");
                if ("news_detail".equals(news_type)) {
                    toolbar_title.setText(getResources().getString(R.string.txt_news_detail));
                } else {
                    toolbar_title.setText(getResources().getString(R.string.txt_more_news));
                }
                toolbar.setNavigationOnClickListener(v -> {
                    if (recyclerView.getVisibility() == View.VISIBLE) {
                        recyclerView.setVisibility(View.GONE);
                        ll_close_category.setVisibility(View.GONE);
                        iv_category.setVisibility(View.VISIBLE);
                        iv_category_close.setVisibility(View.GONE);
                    } else {
                        onBackPressed();
                    }

                });
            } else {
                //show hamburger
                actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

                setToolbarTitle();

                actionBarDrawerToggle.syncState();
                toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
            }
        });

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
            if (fragment instanceof EventsFragment) {
//                ((EventsFragment) fragment).onBackPressed();
                getSupportFragmentManager().popBackStack();
            }
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    // show or hide the fab
    private void toggleFab() {
       /* if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();*/
    }

    @Override
    public void showHamburgerIcon() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    public void showBackIcon() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }

    @Override
    public void onCategorySelected(int id, String title) {
        Log.d(TAG, "onCategorySelected:   " + id + "    " + title);
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        Stash.put("news_type", "more_news");

        recyclerView.setVisibility(View.GONE);
        ll_close_category.setVisibility(View.GONE);
        iv_category.setVisibility(View.VISIBLE);
        iv_category_close.setVisibility(View.GONE);

        /*Fragment someFragment = new CategorywiseDatalFragment(id, title);
         *//* Bundle args = new Bundle();
        args.putString("categoryID", String.valueOf(id));
        someFragment.setArguments(args);*//*
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, someFragment, TAG_HOME); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.detach(someFragment);
        transaction.attach(someFragment);
        transaction.commitAllowingStateLoss();*/

        Intent toDetails = new Intent(this, CategorywiseDetailActivity.class);
        toDetails.putExtra("categoryID", id);
        toDetails.putExtra("title", title);
        startActivity(toDetails);
    }

    @Override
    public void onMenuSelected(int id, String title) {
        if ("about_us".equals(Stash.getString("menu_for"))) {
            Log.d(TAG, "onMenuSelected: " + title);
            // Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            ll_close_category.setVisibility(View.GONE);

            Fragment someFragment = new AboutUsFragment(id, title);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, someFragment); // give your fragment container id in first parameter
            //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            //transaction.detach(someFragment);
            //transaction.attach(someFragment);
            //transaction.commitAllowingStateLoss();
            transaction.commit();
        } else if ("events".equals(Stash.getString("menu_for"))) {
            Log.d(TAG, "onMenuSelected: " + title);
            // Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            ll_close_category.setVisibility(View.GONE);

            Fragment someFragment = new EventsFragment(id, title);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, someFragment); // give your fragment container id in first parameter
            //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            //transaction.detach(someFragment);
            //transaction.attach(someFragment);
            //transaction.commitAllowingStateLoss();
            transaction.commit();
        } else if ("gallery".equals(Stash.getString("menu_for"))) {
            Log.d(TAG, "onMenuSelected: " + title);
            recyclerView.setVisibility(View.GONE);
            ll_close_category.setVisibility(View.GONE);

            Fragment someFragment = new GalleryFragment(id, title);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, someFragment); // give your fragment container id in first parameter
            transaction.commit();
        } else if ("committee".equals(Stash.getString("menu_for"))) {
            Log.d(TAG, "onMenuSelected: " + title);
            recyclerView.setVisibility(View.GONE);
            ll_close_category.setVisibility(View.GONE);


            switch (title) {
                case "Office Bearers":
                    commiteeID = 7;
                    break;


                case "Managing Committee 2020-22":
                    commiteeID = 1;
                    break;


                case "Co-Opted Members":
                    commiteeID = 2;
                    break;


                case "Invitee Members":
                    commiteeID = 3;
                    break;


                case "Special Invitee Members":
                    commiteeID = 4;
                    break;


                case "Advisory Board":
                    commiteeID = 5;
                    break;


                case "Managing Committee 2018-20":
                    commiteeID = 6;
                    break;

                default:
                    commiteeID = 7;
                    break;

            }
            Fragment someFragment = new CommitteeFragment(commiteeID, title);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, someFragment); // give your fragment container id in first parameter
            transaction.commit();
        }

    }

    @Override
    public void showPopUpMenuIcon() {
        iv_category.setVisibility(View.VISIBLE);
        iv_category_close.setVisibility(View.GONE);
    }

    @Override
    public void hidePopUpMenuIcon() {
        iv_category.setVisibility(View.GONE);
        iv_category_close.setVisibility(View.GONE);
    }


    @Override
    public void showHomeLogoIcon() {
        iv_home_logo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHomeLogoIcon() {
        iv_home_logo.setVisibility(View.GONE);
    }


    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String
            language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onLanguageClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_english:
                setNewLocale(this, LocaleManager.ENGLISH);
                break;
            case R.id.tv_gujarati:
                setNewLocale(this, LocaleManager.GUJARATI);
                break;
        }
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            if (!(MainActivityWithLogin.this).isFinishing()) {
                new NewVersionAvailableDialog(this, this).show();
            }
            return;
            //showUpdateDialog();

        }
    }

    @Override
    public void onUpdateDialogItemClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.btn_yes_dialog:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                finish();
                break;
            case R.id.btn_no_dialog:
                break;

        }
    }
}
