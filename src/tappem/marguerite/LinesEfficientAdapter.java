package tappem.marguerite;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LinesEfficientAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private BusStop bus;
	private final int minListItems = 5;
	private Context context;
	private int resource;
	public LinesEfficientAdapter(Context context, BusStop bus, int layoutResource) {
		mInflater = LayoutInflater.from(context);
		this.bus = bus;
		this.context = context;
		resource = layoutResource;

	}

	public int getCount() {

		if(bus == null || bus.getBusLines() == null || (bus.getBusLines().size() < minListItems))
			return minListItems;
		else
			return bus.getBusLines().size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(resource, null);
			holder = new ViewHolder();
			holder.serviceTo = (TextView) convertView.findViewById(R.id.busServiceTo);
			holder.time = (TextView) convertView.findViewById(R.id.busTime);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try{
			BusLine currentLine = bus.getBusLines().get(position);
			//	holder.busLine.setText(currentLine.getLineName());
			//System.out.println("FOR THE LIST: " + currentLine.toString());
			String imageName = currentLine.getIcon();
			ImageView busLineIcon = (ImageView) convertView.findViewById(R.id.busLineImage);
			String uri = "drawable/" + imageName;
			int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

			busLineIcon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), imageResource));


			holder.serviceTo.setText(currentLine.getStatus());




			String minsToBus = currentLine.getEstimatedDepartureTime();
			String txt = "";

			try{
				int mins = Integer.parseInt(minsToBus);
				if(mins <= -1)
				{
					System.out.println("Just Left");
					txt = "Just left";
					holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, 17);
					holder.time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boxgrey));


				}else if(mins <= 1)
				{
					txt = "Coming now";
					holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, 17);
					holder.time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boxgreen));

				}else if(mins <= 5)
				{
					txt = minsToBus;
					holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, 55);
					holder.time.setPadding(0, 0, 0, 0);
					holder.time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boxorange));

				}else if(mins <= 15)
				{
					txt = minsToBus;
					holder.time.setPadding(0, 0, 0, 0);
					holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, 55);
					holder.time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boxred));

				}
				else if(mins <= 59)
				{
				//	System.out.println("WEIRD");
					txt = minsToBus;
					holder.time.setPadding(0, 0, 0, 0);
					holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, 55);
					holder.time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boxgrey));

				}else
				{
					txt = "More than 1 hour";
					holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, 17);
					holder.time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boxgrey));

				}

			}catch(Exception e)
			{
				if(minsToBus.equals(MargueriteTransportation.noMoreBuses))
				{

					txt = minsToBus;
					holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, 17);
					holder.time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.boxgrey));

				}
			}

			holder.time.setText(txt);
		}catch(Exception e)
		{
			holder.time.setText("");
			
			holder.time.setBackgroundColor(0x0000000);
			holder.serviceTo.setText("");
		}
		return convertView;
	}

	static class ViewHolder {
		TextView busLine;
		TextView serviceTo;
		TextView time;
	}
}
