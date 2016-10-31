package layout;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.TaskActivityInterface;

public class TabThreeFragment extends Fragment implements TaskActivityInterface {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_three, container, false);
    }
}
