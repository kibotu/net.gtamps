package net.gtamps.android.graphics.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 16:25
 */
public class TwoLayoutsArrayAdapter extends ArrayAdapter<String> {
    private final String[] values;
    private LayoutInflater inflator;

    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }

    public TwoLayoutsArrayAdapter(Context context, String[] values) {
        super(context, R.id.TextView01, values);
        this.values = values;
        inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position % 2 == 0) ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            if (getItemViewType(position) == 0) {
                rowView = inflator.inflate(R.layout.row_even, null);
            } else {
                rowView = inflator.inflate(R.layout.row_odd, null);
            }
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.TextView01);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.ImageView01);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.text.setText(values[position]);
        return rowView;
    }
}
