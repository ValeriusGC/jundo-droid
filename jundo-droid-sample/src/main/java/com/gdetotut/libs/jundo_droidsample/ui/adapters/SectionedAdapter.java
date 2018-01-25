package com.gdetotut.libs.jundo_droidsample.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdetotut.libs.jundo_droidsample.R;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import com.gdetotut.libs.jundo_droidsample.model.TypeOf;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by valerius on 30.06.17.
 *
 * @author valerius
 */
public class SectionedAdapter extends SectioningAdapter {

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
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }

    /**
     * Part of more base class BriefNote
     */
    private static class Item {
        final TypeOf.Oid oid;
        final String title;

        public Item(TypeOf.Oid oid, String title) {
            this.oid = oid;
            this.title = title;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    /**
     * Section of items
     */
    private static class Section {
        final ArrayList<Item> items = new ArrayList<>();
    }

    /**
     * Inner information
     */
    class ItemInfo {
        final int sectionIdx;
        final int itemIdx;
        /**
         * Presents absolute position in the adapter.
         * Will get set when {@link org.zakariya.stickyheaders.SectioningAdapter.ViewHolder} will being bound to the View.
         */
        int pos = -1;

        public ItemInfo(int sectionIdx, int itemIdx) {
            this.sectionIdx = sectionIdx;
            this.itemIdx = itemIdx;
        }
    }

    final List<String> caps = new ArrayList<>();
    final List<Section> secs = new ArrayList<>();
    final Map<String, ItemInfo> oid2info = new TreeMap<>();
//    List<String> captions = new ArrayList<>();
//    Map<Object, ItemInfo> cursors = new HashMap<>();

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
        caps.clear();
        secs.clear();
        oid2info.clear();

        // Задача - раскидать заметки по секциям
        //  -------------------------------------
        //  Если есть заметки
        //      Взять из заметки дату
        //      По дате найти секцию или создать новую и добавить в карту
        //      Добавить заметку в секцию
        if(notes.size() > 0){
            for (int i=0; i< notes.size(); ++i) {
                BriefNote note = notes.get(i);
                Item item = new Item(note.getOid(), note.getTitle());
                Date date = new Date(note.getTime());
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String cap = sdfDate.format(date);
                int capIdx = caps.indexOf(cap);
                Section sec;
                if(capIdx < 0){
                    sec = new Section();
                    caps.add(cap);
                    secs.add(sec);
                }else {
                    sec = secs.get(capIdx);
                }
                sec.items.add(item);
                oid2info.put(note.getTitle(), new ItemInfo(secs.size() - 1, sec.items.size() - 1));
            }
        }
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public int getNumberOfSections() {
        return secs.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return secs.get(sectionIndex).items.size();
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
        Section s = secs.get(sectionIndex);
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        Item item = s.items.get(itemIndex);
        ((ItemViewHolder) viewHolder).itemView.setTag(item);
        ivh.personNameTextView.setText(item.title);
        System.out.println("I.getAdapterPosition():" + viewHolder.getAdapterPosition());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = secs.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        ((HeaderViewHolder) viewHolder).itemView.setTag(s);
        hvh.titleTextView.setText(caps.get(sectionIndex));
        System.out.println("S.getAdapterPosition():" + viewHolder.getAdapterPosition());
    }

    public List<TypeOf.Oid> getOids(Object obj) {
        final List<TypeOf.Oid> oids = new ArrayList<>();
        if(obj instanceof Item) {
            oids.add((((Item) obj).oid));
        }else if(obj instanceof Section) {
            for (Item it : ((Section) obj).items) {
                oids.add(it.oid);
            }
        }
        return oids;
    }

}
