package be.pxl.ja2.jdbc;

import be.pxl.ja2.jdbc.model.MusicDatasource;

public class InsertSong {

    public static void main(String[] args) {
        MusicDatasource datasource = new MusicDatasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        datasource.insertSong("Bird Dog", "Everly Brothers", "All-Time Greatest Hits", 7);
        datasource.close();
    }
}


















