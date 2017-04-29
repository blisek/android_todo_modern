package com.blisek.todo_list.new_task.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blisek.todo_list.R;
import com.blisek.todo_list.new_task.constants.DetailedTaskActivityConstants;
import com.blisek.todo_list.persistence.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailedTaskActivity extends AppCompatActivity {
    private static final String TAG = "DetailedTaskActivity";

    // region CHILD VIEWS --------------------------------------------------------------------------

    @BindView(R.id.title_field)
    EditText titleField;

    @BindView(R.id.task_description_field)
    EditText descriptionField;

    @BindView(R.id.end_date_field)
    EditText endDateField;

    @BindView(R.id.save_btn)
    Button saveButton;

    @BindView(R.id.edit_bnt)
    Button editButton;

    @BindView(R.id.delete_bnt)
    Button deleteButton;

    @BindView(R.id.end_date_select)
    ImageButton endDateSelectButton;

    // endregion -----------------------------------------------------------------------------------

    // region VARIABLES ----------------------------------------------------------------------------

    private Calendar creationDateCalendar;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private Task task;

    // endregion

    // region ANDROID METHODS ----------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_task);
        ButterKnife.bind(this);
        init(getIntent().getExtras());
    }

    // endregion -----------------------------------------------------------------------------------

    // region INIT METHODS -------------------------------------------------------------------------

    private void init(Bundle extras) {
        restrictTitleLength();
        dateFormatter = new SimpleDateFormat(DetailedTaskActivityConstants.END_DATE_FORMAT);
        constructDatePickerDialog();
        updateEndDateField();
        parseExtras(extras);
    }

    private void parseExtras(Bundle extras) {
        if(extras == null)
            return;

        Task task = (Task)extras.getSerializable(DetailedTaskActivityConstants.EXTRA_TASK_PARAM);
        if(task == null)
            return;

        this.task = task;
        titleField.setText(task.title);
        descriptionField.setText(task.description);
        if(task.endDate > 0l) {
            Date d = new Date(task.endDate);
            endDateField.setText(dateFormatter.format(d));
        }

        deleteButton.setEnabled(true);
    }

    // endregion -----------------------------------------------------------------------------------

    // region CLICK LISTENERS ----------------------------------------------------------------------

    @OnClick(R.id.end_date_select)
    public void onEndDateSelectBtnClick(View view)
    {
        datePickerDialog.show();
    }

    @OnClick(R.id.save_btn)
    public void onSaveBtnClick(View view)
    {
        setFocusOnSaveButton();
        // TODO: verificate description
        if(task == null)
            task = new Task();

        if (!setTaskEndDate(task)) return;
        if (!setDescription(task)) return;
        setTitle(task);
        setCreatedDate(task);

        try {
            task.save();
            enableControls();
        }
        catch (Exception ex) {
            Log.e(TAG, "While saving object.", ex);
        }
    }

    @OnClick(R.id.edit_bnt)
    public void onEditBtnClick(View view) {
        enableControls();
    }

    @OnClick(R.id.delete_bnt)
    public void onDeleteBtnClick(View view) {
        task.delete();
        deleteButton.setEnabled(false);
    }

    // endregion -----------------------------------------------------------------------------------

    // region FIELD SETTERS ------------------------------------------------------------------------

    private boolean setTaskEndDate(Task task) {
        try {
            Date endDate = dateFormatter.parse(endDateField.getText().toString());
            task.endDate = endDate.getTime();
            endDateField.setError(null);
        } catch (ParseException ex) {
            endDateField.setError(getString(R.string.detailed_task_invalid_date_format));
            return false;
        }
        return true;
    }

    private boolean setDescription(Task task) {
        CharSequence descriptionCS = descriptionField.getText();
        if(descriptionCS.length() < 0) {
            descriptionField.setError(getString(R.string.detailed_task_description_field_hint));
            return false;
        }

        task.description = descriptionCS.toString();
        return true;
    }

    private void setTitle(Task task) {
        String title = getOrTrimTitle();
        task.title = title;
        titleField.setText(title);
    }

    private void setCreatedDate(Task task) {
        task.creationDate = creationDateCalendar.getTime().getTime();
    }

    // endregion -----------------------------------------------------------------------------------

    // region VIEWS STATE MODIFIERS ----------------------------------------------------------------

    private void enableControls() {
        titleField.setEnabled(!titleField.isEnabled());
        descriptionField.setEnabled(!descriptionField.isEnabled());
        endDateField.setEnabled(!endDateField.isEnabled());
        endDateSelectButton.setEnabled(!endDateSelectButton.isEnabled());

        deleteButton.setEnabled(task != null && task.getId() != null);
        editButton.setEnabled(!editButton.isEnabled());
    }

    private void setFocusOnSaveButton() {
        titleField.clearFocus();
        descriptionField.clearFocus();
        endDateField.clearFocus();
        saveButton.requestFocus();
    }

    private void restrictTitleLength() {
        InputFilter[] filters = new InputFilter[] {
                new InputFilter.LengthFilter(DetailedTaskActivityConstants.TITLE_MAX_LENGTH)
        };
        titleField.setFilters(filters);
    }

    private void updateEndDateField() {
        endDateField.setText(dateFormatter.format(creationDateCalendar.getTime()));
    }

    // endregion

    // region HELPER METHODS -----------------------------------------------------------------------

    private String getOrTrimTitle() {
        String title = titleField.getText().toString();

        if("".equals(title))
            title = descriptionField.getText().toString();

        if(title.length() < DetailedTaskActivityConstants.TITLE_MAX_LENGTH) {
            return title;
        }
        else {
            return title.substring(0, DetailedTaskActivityConstants.TITLE_MAX_LENGTH-3) + "...";
        }
    }

    private void constructDatePickerDialog() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                creationDateCalendar.set(year, month, dayOfMonth);
                updateEndDateField();
            }
        };
        creationDateCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, R.style.Theme_AppCompat_Light_Dialog,
                onDateSetListener, creationDateCalendar.get(Calendar.YEAR), creationDateCalendar.get(Calendar.MONTH),
                creationDateCalendar.get(Calendar.DAY_OF_MONTH));
    }

    // endregion

}
