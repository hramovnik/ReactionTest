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
            R.drawable.p25, R.drawable.p26, R.drawable.p27, R.drawable.p28,
            R.drawable.p29, R.drawable.p30, R.drawable.p31, R.drawable.p32,
            R.drawable.p33, R.drawable.p34, R.drawable.p35, R.drawable.p36,
            R.drawable.p37, R.drawable.p38, R.drawable.p39, R.drawable.p40,
            R.drawable.p41, R.drawable.p42, R.drawable.p43, R.drawable.p44,
            R.drawable.p45, R.drawable.p46, R.drawable.p47, R.drawable.p48,
            R.drawable.p49, R.drawable.p50, R.drawable.p51, R.drawable.p52,
            R.drawable.p53, R.drawable.p54, R.drawable.p55, R.drawable.p56,
            R.drawable.p57, R.drawable.p58, R.drawable.p59, R.drawable.p60,
            R.drawable.p61, R.drawable.p62, R.drawable.p63, R.drawable.p64
    };

    static String[] names = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        names = new String[imageId.length];
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
