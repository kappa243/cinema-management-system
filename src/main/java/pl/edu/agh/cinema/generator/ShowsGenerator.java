package pl.edu.agh.cinema.generator;

import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.room.Room;
import pl.edu.agh.cinema.model.sales.Sales;
import pl.edu.agh.cinema.model.show.Show;
import pl.edu.agh.cinema.model.show.ShowRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ShowsGenerator {

    public static List<Show> generateShow(List<Movie> movies, List<Room> rooms){
        List<Show> shows=new ArrayList<>();
//        Show show1 = new Show(Timestamp.valueOf("2023-11-12 11:00:03.123456789").toLocalDateTime(), Timestamp.valueOf("2023-11-12 13:02:03.123456789").toLocalDateTime(), Timestamp.valueOf("2018-11-12 01:02:03.123456789").toLocalDateTime(), 12, 38);
        List<LocalDateTime> times= Arrays.asList();
        LocalDateTime rano=Timestamp.valueOf("2022-12-01 10:00:00").toLocalDateTime();
        LocalDateTime popoludnie=Timestamp.valueOf("2022-12-01 17:00:00").toLocalDateTime();
        LocalDateTime wieczor=Timestamp.valueOf("2022-12-01 20:00:00").toLocalDateTime();
        LocalDateTime noc=Timestamp.valueOf("2022-12-01 22:00:00").toLocalDateTime();
        List<LocalDateTime> terminy= Arrays.asList(rano, popoludnie, wieczor, noc);
        Random rand=new Random();
        for(int i=0; i<45; i++){
            for(var r:rooms){
                for(var t:terminy){
                    Movie m=movies.get(rand.nextInt(movies.size()));
                    Show seans = new Show(t.plusDays(i), t.plusDays(i).plusMinutes(m.getDuration()+30), t.plusDays(i).minusDays(5), rand.nextInt(30)+12, 0);
                    seans.setMovie(m);
                    seans.setRoom(r);
                    shows.add(seans);
                }
            }
        }
        return shows;
    }

    //funkcja symulująca sprzedaż biletów w przeciagu godziny
    public static List<Sales> generateSales(List<Show> shows){
        List<Sales> salesList=new ArrayList<>();
        LocalDateTime startDate=Timestamp.valueOf("2022-12-01 10:00:00").toLocalDateTime().minusDays(5);
        for(int h=0;;h++){
            LocalDateTime current=startDate.plusHours(h);
            if(LocalDateTime.now().isBefore(current))break;
            int hour=current.getHour();
            if(hour<8 || hour>22)continue;
            List<Show> activeShows=shows.stream().filter(s->s.getSellTicketsFrom().isBefore(current) && s.getEndTime().isAfter(current)).collect(Collectors.toList());
            for(var as:activeShows){
                Room r=as.getRoom();
                int capacity=r.getSeatsNumber();
                int soldTickets=as.getSoldTickets();
                if(soldTickets==capacity)continue;
                int deltaSales=0;
                if(as.getStartTime().getDayOfMonth()==current.getDayOfMonth()){
                    int diff=capacity-soldTickets;
                    deltaSales=(int)(0.2*diff);
                }
                else{
                    int diff=capacity-soldTickets;
                    deltaSales=(int)(0.02*diff);
                }
                as.setSoldTickets(soldTickets+deltaSales);
                Sales sales=new Sales();
                sales.setShow(as);
                sales.setStartTime(current.minusHours(1));
                sales.setEndTime(current);
                sales.setSoldTickets(deltaSales);
                salesList.add(sales);
            }
        }
        return salesList;
    }
}
