package com.stl.gestion_contacts;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Cette classe gère les objets qui doivent être sauvegardés dans la mémoire.
 * Elle a un adaptateur qui permet d'afficher ces objets sous forme de liste.
 * C'est pourquoi les objets doivent implémenter getText() de l'interface Printable, qui returne un texte
 * @param <T> Type de l'objet
 */
public class ObjectManager<T> {

    private ArrayList<T> objectsList;
    private ArrayAdapter<T> objectAdapter;

    private Context context;
    private InternalStorage internalStorage;
    private final String filename;

    @SuppressWarnings("unchecked")
    ObjectManager(Context context, String filename, int resource) {

        this.context = context;
        this.filename = filename;
        internalStorage = new InternalStorage(context);
        try {
            // Retrieve the list from internal storage
            objectsList = (ArrayList<T>) internalStorage.readObject(
                    filename,
                    "MainActivity onCreate");
        }
        catch (IOException e) {
            objectsList = new ArrayList();
        }

        objectAdapter = new ObjectAdapter(
                context,
                resource,
                objectsList);

    }

    public void addObject(T t) {
        objectsList.add(t);
        objectAdapter.notifyDataSetChanged();
        saveObjects();
    }

    public void removeObject(int position) {
        objectsList.remove(position);
        objectAdapter.notifyDataSetChanged();
        saveObjects();
    }

    private void saveObjects () {
        // Save the list of entries to internal storage
        internalStorage.writeObject(
                filename,
                objectsList,
                "saveObjects() MainActivity");
    }

    public ArrayList<T> getObjectsList() {
        return objectsList;
    }

    public ArrayAdapter<T> getObjectAdapter() {
        return objectAdapter;
    }
}