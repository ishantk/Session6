package co.edureka.session6;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ishantkumar on 19/11/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    Context context;
    int resource;
    List<Book> objects;

    public BookAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(resource,parent,false);


        TextView txtPrice = view.findViewById(R.id.textViewPrice);
        TextView txtName = view.findViewById(R.id.textViewName);
        TextView txtAuthor = view.findViewById(R.id.textViewAuthor);

        Book book = objects.get(position);

        txtPrice.setText(book.getPrice());
        txtName.setText(book.getName());
        txtAuthor.setText(book.getAuthor());


        return view;
    }
}
