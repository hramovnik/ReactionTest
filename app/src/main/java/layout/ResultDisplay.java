package layout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;


public class ResultDisplay extends DialogFragment  implements View.OnClickListener {

    TextView tvFrameResult = null;
    static String str ="";
    Session currentSession = null;
    LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getDialog().setTitle("Результат");
        return inflater.inflate(R.layout.fragment_result_display, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = (LinearLayout) getView().findViewById(R.id.resultDisplayLayout);

        Button buttonSave = ((Button) getView().findViewById(R.id.frameResultButtonSave));
        buttonSave.setOnClickListener(this);

        tvFrameResult = ((TextView) getView().findViewById(R.id.tvFrameResult));
        tvFrameResult.setText(str);

    }

    public void setText(String value){
        str = value;
    }

    public void show(FragmentManager manager, String tag, String information){
        super.show(manager,tag);
        setText(information);
    }


    @Override
    public void onClick(View v) {
        dismiss();
        str ="";
    }

}
