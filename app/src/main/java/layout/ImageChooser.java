package layout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hramovnik.reactiontest.MainActivity;
import com.hramovnik.reactiontest.R;
import com.hramovnik.reactiontest.Session;


public class ImageChooser extends DialogFragment{

    private int choosedIndex = 1;
    private ListView listView = null;
    public final static Integer[] imageId = {
            R.drawable.p01, R.drawable.p02, R.drawable.p03, R.drawable.p04,
            R.drawable.p05, R.drawable.p06, R.drawable.p07, R.drawable.p08,
            R.drawable.p09, R.drawable.p10, R.drawable.p11, R.drawable.p12,
            R.drawable.p13, R.drawable.p14, R.drawable.p15, R.drawable.p16,
            R.drawable.p17, R.drawable.p18, R.drawable.p19, R.drawable.p20,
            R.drawable.p21, R.drawable.p22, R.drawable.p23, R.drawable.p24,
            R.drawable.p25, R.drawable.p26
    };

    static String[] names = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        names = new String[26];
        for(int i = 0; i < names.length; i++){
            names[i] = "Изображение " + String.valueOf(i+1);
        }
        getDialog().setTitle("Выбор изображения");
        return inflater.inflate(R.layout.fragment_image_chooser, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.ImageChooserListView);

        CustomList adapter = new CustomList(getActivity(), names, imageId);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                choosedIndex = position;
                //Toast.makeText(getActivity(), "You Clicked at " + names[position], Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });


    }

    public void show(FragmentManager manager, String tag, int index){
        choosedIndex = index;
        super.show(manager,tag);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), choosedIndex, getActivity().getIntent());
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), choosedIndex, getActivity().getIntent());
        super.onCancel(dialog);

    }

    public void onClick(View v) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), choosedIndex, getActivity().getIntent());
        dismiss();
    }

}
