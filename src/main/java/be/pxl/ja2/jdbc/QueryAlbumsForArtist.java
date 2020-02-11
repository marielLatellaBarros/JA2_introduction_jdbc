package be.pxl.ja2.jdbc;

import be.pxl.ja2.jdbc.model.MusicDatasource;

import java.util.List;

public class QueryAlbumsForArtist {

    public static void main(String[] args) {
        MusicDatasource datasource = new MusicDatasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        List<String> albumsForArtist =
                datasource.queryAlbumsForArtist("Iron Maiden", MusicDatasource.ORDER_BY_ASC);

        for(String album : albumsForArtist) {
            System.out.println(album);
        }

        datasource.close();
    }
}
