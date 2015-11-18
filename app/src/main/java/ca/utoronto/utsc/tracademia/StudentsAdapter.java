package ca.utoronto.utsc.tracademia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provide views to RecyclerView with data from mStudents.
 */
public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "StudentsAdapter";

    private List<Student> mStudents;
    private List<Student> mFilteredStudents;
    private OnStudentSelectedListener mCallback;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param students String[] containing the data to populate views to be used by RecyclerView.
     */
    public StudentsAdapter(List<Student> students, OnStudentSelectedListener callback) {
        mStudents = students;
        mFilteredStudents = new ArrayList<>(mStudents);
        mCallback = callback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v, mCallback);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        Student student = mFilteredStudents.get(position);
        viewHolder.mainText.setText(student.getDisplayName());
        viewHolder.subText.setText("XP: " + student.getExperiencePoints() + "    CP: " + student.getChallengePoints() + "    RP: " + student.getTeachingPoints());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFilteredStudents.size();
    }

    public void addItemsToList(Student... students) {
        mStudents.clear();
        mStudents.addAll(Arrays.asList(students));
        mFilteredStudents = new ArrayList<>(mStudents);
        notifyDataSetChanged();
    }

    public List<Student> getStudents() {
        return mStudents;
    }

    public List<Student> getFilteredStudents() {
        return mFilteredStudents;
    }

    public Student getStudentByStudentNumber(String studentNumber) {
        if (studentNumber != null) {
            for (Student student : mStudents) {
                if (studentNumber.equals(student.getStudentNumber())) {
                    return student;
                }
            }
        }
        return null;
    }

    @Override
    public Filter getFilter() {
        return new StudentsFilter();
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mainText;
        private TextView subText;
        private OnStudentSelectedListener mCallback;

        public ViewHolder(View v, OnStudentSelectedListener callback) {
            super(v);
            mainText = (TextView) v.findViewById(R.id.maintext);
            subText = (TextView) v.findViewById(R.id.subtext);
            mCallback = callback;

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onStudentSelected(getAdapterPosition());
                }
            });
        }
    }

    class StudentsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            mFilteredStudents.clear();

            if (constraint.length() == 0) {
                mFilteredStudents.addAll(mStudents);
            } else {
                for (Student student : mStudents) {
                    if (student.getDisplayName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        mFilteredStudents.add(student);
                    }
                }
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}
