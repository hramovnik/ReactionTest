package layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;
import com.hramovnik.reactiontest.SessionImages;
import com.hramovnik.reactiontest.SessionTapping;
import com.hramovnik.reactiontest.TaskActivityInterface;


public class TabFiveFragment extends TabFragment implements TaskActivityInterface, View.OnClickListener {

    ImageButton [] imageButton = new ImageButton[2];
    ImageChooser dialogImageChooser = null;
    int [] choosedIndex = new int[2];


    private SeekBar sbExposition;
    private TextView teExposition;
    private final String expositionTag = "PICTEST_IMAGE_EXPOSITION";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_five, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        teExposition = (TextView) getView().findViewById(R.id.teExpositionTime);
        sbExposition = getSb(teExposition, R.id.sbExpositionTime, 1,120,expositionTag);

        imageButton[0] = (ImageButton) getView().findViewById(R.id.fragmentFiveImageButtonL);
        imageButton[1] = (ImageButton) getView().findViewById(R.id.fragmentFiveImageButtonR);
        for (int i = 0; i < 2; i++) {
            imageButton[i].setOnClickListener(this);
            choosedIndex[i] = sp.getInt("PICTEST_IMAGE_" + String.valueOf(i), 0);
            if ((choosedIndex[i] >= ImageChooser.imageId.length)||(choosedIndex[i] < 0)) choosedIndex[i] = 0;
            imageButton[i].setImageResource(ImageChooser.imageId[choosedIndex[i]]);
        }
        dialogImageChooser = new ImageChooser();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt("PICTEST_IMAGE_0", choosedIndex[0]);
            ed.putInt("PICTEST_IMAGE_1", choosedIndex[1]);
            ed.putInt("PICTEST_IMAGE_EXPOSITION", sbExposition.getProgress());
            ed.apply();
        }catch (Exception e){

        }

    }

    @Override
    public Session getSession() {
        return new SessionImages(choosedIndex[0] + 1, choosedIndex[1] + 1, sbExposition.getProgress()*1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        choosedIndex[requestCode] = resultCode;
        if ((choosedIndex[requestCode] >= ImageChooser.imageId.length)||(choosedIndex[requestCode] < 0)) choosedIndex[requestCode] = 0;
        imageButton[requestCode].setImageResource(ImageChooser.imageId[choosedIndex[requestCode]]);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragmentFiveImageButtonL:
                dialogImageChooser.setTargetFragment(this, 0);
                dialogImageChooser.show(getFragmentManager(), "");
                break;
            case R.id.fragmentFiveImageButtonR:
                dialogImageChooser.setTargetFragment(this, 1);
                dialogImageChooser.show(getFragmentManager(), "");
                break;
            default:
        }
    }
}
