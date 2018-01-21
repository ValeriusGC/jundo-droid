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
import java.util.Map;

import static com.gdetotut.libs.jundo_droidsample.ui.adapters.SectionedAdapter.SubjInfo.IS_ITEM;
import static com.gdetotut.libs.jundo_droidsample.ui.adapters.SectionedAdapter.SubjInfo.IS_SECTION;

/**
 * Created by valerius on 30.06.17.
 *
 * @author valerius
 */
public class SectionedAdapter extends SectioningAdapter {

    private class Section {
        ArrayList<BriefNote> notes = new ArrayList<>();
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

    /**
     * Inner information
     */
    static class SubjInfo {
        static final boolean IS_SECTION = true;
        static final boolean IS_ITEM = true;
        final boolean isSection;
        final Object subj;
        final int sectionIdx;
        final int itemIdx;

        public SubjInfo(boolean isSection, Object subj, int sectionIdx, int itemIdx) {
            this.isSection = isSection;
            this.subj = subj;
            this.sectionIdx = sectionIdx;
            this.itemIdx = itemIdx;
        }
    }

    List<Section> sections = new ArrayList<>();
    List<String> captions = new ArrayList<>();
    Map<Object, SubjInfo> cursors = new HashMap<>();

    // Сущность должна соответствовать задачам адаптера:
    // <Object, Entity>, где
    //  Object - это адрес, на который нажали вл вьюхе,
    //  Entity - тот объект, который построен адаптером - секция или элемент.

    //ArrayList<Section> mSections = new ArrayList<>();

    public SectionedAdapter() {
    }

    public void delete(Object o) {

        if(o instanceof BriefNote) {

        }else if(o instanceof Section) {

        }
    }

    public void setNotes(List<BriefNote> notes) {
        sections.clear();
        cursors.clear();

        // Задача - раскидать заметки по секциям
        //  -------------------------------------
        //  Если есть заметки
        //      Взять из заметки дату
        //      По дате найти секцию или создать новую и добавить в карту
        //      Добавить заметку в секцию
        if(notes.size() > 0){
            for (int i=0; i< notes.size(); ++i) {
                BriefNote note = notes.get(i);
                Date date = new Date(note.getTime());
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String cap = sdfDate.format(date);
                int idx = captions.indexOf(cap);
                Section sec = null;
                if(idx < 0){
                    captions.add(cap);
                    sec = new Section();
                    sections.add(sec);
                    cursors.put(sec, new SubjInfo(IS_SECTION, sec, sections.size() - 1, -1));
                }else {
                    sec = sections.get(idx);
                }
                sec.notes.add(note);
                cursors.put(sec, new SubjInfo(IS_ITEM, note, sections.size() - 1, sec.notes.size() - 1));
            }
        }
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).notes.size();
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
        Section s = sections.get(sectionIndex);
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        BriefNote person = s.notes.get(itemIndex);
        ((ItemViewHolder) viewHolder).itemView.setTag(person);
        ivh.personNameTextView.setText(person.getTitle());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = sections.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        ((HeaderViewHolder) viewHolder).itemView.setTag(s);
        hvh.titleTextView.setText(captions.get(sectionIndex));
    }



}
