package layout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hramovnik.reactiontest.R;


public class ResultDisplay extends DialogFragment  implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Результат");
        View v = inflater.inflate(R.layout.fragment_result_display, null);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        ((Button) getView().findViewById(R.id.frameResultButtonSave)).setOnClickListener(this);

    }

    @Override
    public void onResume(){
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, getActivity().getIntent());
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
