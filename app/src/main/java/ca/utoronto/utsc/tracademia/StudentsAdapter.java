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
    private MainActivity mCallback;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param students String[] containing the data to populate views to be used by RecyclerView.
     */
    public StudentsAdapter(List<Student> students, MainActivity callback) {
        mStudents = students;
        mFilteredStudents = new ArrayList<>(mStudents);
        mCallback = callback;
    }

    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create new views (invoked by the layout manager)
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Replace the contents of a view (invoked by the layout manager)
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        Student student = mFilteredStudents.get(position);
        viewHolder.mainText.setText(student.getUsername());
    }


    @Override
    public int getItemCount() {
        // Return the size of your dataset (invoked by the layout manager)
        return mFilteredStudents.size();
    }

    /**
     * Sets the students and filtered students list. Clears previous list.
     *
     * @param students to set.
     */
    public void setStudents(Student... students) {
        mStudents.clear();
        mStudents.addAll(Arrays.asList(students));
        mFilteredStudents = new ArrayList<>(mStudents);
        notifyDataSetChanged();
    }

    /**
     * @return filtered students.
     */
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

    /**
     * @return filter for students.
     */
    @Override
    public Filter getFilter() {
        return new StudentsFilter();
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mainText;

        public ViewHolder(View view) {
            super(view);
            mainText = (TextView) view.findViewById(R.id.maintext);

            // Define click listener for the ViewHolder's View.
            view.setOnClickListener(new View.OnClickListener() {
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
                String filter = constraint.toString().toLowerCase();
                for (Student student : mStudents) {
                    if (student.getUsername().toLowerCase().startsWith(filter)) {
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
