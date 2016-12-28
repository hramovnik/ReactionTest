package layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;
import com.hramovnik.reactiontest.SessionTapping;
import com.hramovnik.reactiontest.TaskActivityInterface;


public class TabFourFragment extends TabFragment implements TaskActivityInterface, View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tab_four, container, false);
    }

    ImageButton imageButton = null;
    ImageChooser dialogImageChooser = null;
    int choosedIndex = 0;

    private SeekBar sbInterval;
    private TextView teInterval;
    private final String intervalTag = "TT_INTERVAL";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        teInterval = (TextView) getView().findViewById(R.id.teInterval);
        sbInterval = getSb(teInterval, R.id.sbInterval, 1,12,intervalTag);

        imageButton = (ImageButton)getView().findViewById(R.id.fragmentFourImageButton);
        imageButton.setOnClickListener(this);

        choosedIndex = sp.getInt("TAPTEST_IMAGE_1", 0);
        if ((choosedIndex > 25)||(choosedIndex < 0)) choosedIndex = 0;

        imageButton.setImageResource(ImageChooser.imageId[choosedIndex]);
        dialogImageChooser = new ImageChooser();

    }

    @Override
    public Session getSession() {
        return new SessionTapping(choosedIndex+1, sbInterval.getProgress());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt("TAPTEST_IMAGE_1", choosedIndex);
            ed.apply();
        }catch (Exception e){

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        choosedIndex = resultCode;
        if (choosedIndex > 25) choosedIndex = 0;

        imageButton.setImageResource(ImageChooser.imageId[choosedIndex]);

    }

    @Override
    public void onClick(View view) {
        dialogImageChooser.setTargetFragment(this, 1);
        dialogImageChooser.show(getFragmentManager(), "Test");
    }


}
