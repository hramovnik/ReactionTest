package layout;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;
import com.hramovnik.reactiontest.TaskActivityInterface;

public class TabTwoFragment extends Fragment implements TaskActivityInterface {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_two, container, false);
    }

    @Override
    public Session getSession() {
        return null;
    }

 /*   @Override
    public void onResume(){
        Log.d("Tag", "Tab2 on resume");
        super.onResume();
    }*/
}
