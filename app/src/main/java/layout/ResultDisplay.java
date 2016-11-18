package layout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;


public class ResultDisplay extends DialogFragment  implements View.OnClickListener {

    TextView tvFrameResult = null;
    String str ="Void";
    Session currentSession = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Результат");
        return inflater.inflate(R.layout.fragment_result_display, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);

         Button buttonSave = ((Button) getView().findViewById(R.id.frameResultButtonSave));
         buttonSave.setOnClickListener(this);

        tvFrameResult = ((TextView) getView().findViewById(R.id.tvFrameResult));
        tvFrameResult.setText(str);

    }

    public void setText(String value){
        str = value;
    }

    public void show(FragmentManager manager, String tag, String information, Session session){
        currentSession = session;
        super.show(manager,tag);
        setText(information);
    }


    @Override
    public void onResume(){
        super.onResume();
    }



    @Override
    public void onClick(View v) {

        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

}
