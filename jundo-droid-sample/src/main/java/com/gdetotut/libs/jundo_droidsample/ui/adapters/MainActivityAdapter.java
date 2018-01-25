package com.gdetotut.libs.jundo_droidsample.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdetotut.libs.jundo_droidsample.R;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import org.zakariya.stickyheaders.SectioningAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by valerius on 30.06.17.
 *
 * @author valerius
 */
public class MainActivityAdapter extends SectioningAdapter {

    private class Section {
        String date;
        ArrayList<BriefNote> notes = new ArrayList<>();

        @Override
        public String toString() {
            return "Section{" +
                    date +
                    ", items=" + notes.size() +
                    '}';
        }
    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {
        TextView personNameTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            personNameTextView = (TextView) itemView.findViewById(R.id.personNameTextView);
        }
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView titleTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    List<BriefNote> mNotes;
    LinkedHashMap<String, Section> mSections = new LinkedHashMap<>();

    List<Section> sections = new ArrayList<>();

    // Сущность должна соответствовать задачам адаптера:
    // <Object, Entity>, где
    //  Object - это адрес, на который нажали вл вьюхе,
    //  Entity - тот объект, который построен адаптером - секция или элемент.

    //ArrayList<Section> mSections = new ArrayList<>();

    public MainActivityAdapter() {
    }

    public List<BriefNote> getNotes() {
        return mNotes;
    }

    public void delete(Object o) {

        if(o instanceof BriefNote) {

        }else if(o instanceof Section) {

        }
    }

    public void setNotes(List<BriefNote> notes) {
        this.mNotes = notes;
        mSections.clear();

        // Задача - раскидать заметки по секциям
        //  -------------------------------------
        //  Если есть заметки
        //      Взять из заметки дату
        //      По дате найти секцию или создать новую и добавить в карту
        //      Добавить заметку в секцию
        if(this.mNotes.size() > 0){
            for (int i=0; i< mNotes.size(); ++i) {
                BriefNote note = mNotes.get(i);
                Date date = new Date(note.getTime());
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String strDate = sdfDate.format(date);
                Section currSection = mSections.get(strDate);;
                if(currSection == null){
                    currSection = new Section();
                    currSection.date = strDate;
                    mSections.put(strDate, currSection);
                }
                currSection.notes.add(note);
            }
        }
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public int getNumberOfSections() {
        return mSections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        Section value = (new ArrayList<>(mSections.values())).get(sectionIndex);
        return value.notes.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Section s = (new ArrayList<>(mSections.values())).get(sectionIndex);
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        BriefNote person = s.notes.get(itemIndex);
        ((ItemViewHolder) viewHolder).itemView.setTag(person);
        ivh.personNameTextView.setText(person.getTitle());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = (new ArrayList<>(mSections.values())).get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        ((HeaderViewHolder) viewHolder).itemView.setTag(s);
        hvh.titleTextView.setText(s.date);
    }



}
