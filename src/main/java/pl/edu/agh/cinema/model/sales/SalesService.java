package pl.edu.agh.cinema.model.sales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.movie.MovieRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


@Service
public class SalesService {

    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Map<LocalDateTime, Integer> getSoldTickets(LocalDateTime startTime, LocalDateTime endTime, Movie m){
        Map<LocalDateTime, Integer> map=new TreeMap<>();
        for(;startTime.isBefore(endTime);startTime = startTime.plusDays(1)){
            int year= startTime.getYear();
            int month= startTime.getMonthValue();
            int day= startTime.getDayOfMonth();
            int salesValue=salesRepository.getSoldTicketsForMovieAndDay(day, month, year, (long)m.getId());
            map.put(startTime, salesValue);

        }
        return map;
    }

    public Map<LocalDateTime, Integer> getSoldTicketsValue(LocalDateTime startTime, LocalDateTime endTime, Movie m){
        Map<LocalDateTime, Integer> map=new TreeMap<>();
        for(;startTime.isBefore(endTime);startTime = startTime.plusDays(1)){
            int year= startTime.getYear();
            int month= startTime.getMonthValue();
            int day= startTime.getDayOfMonth();
            int salesValue=salesRepository.getSoldTicketsValueForMovieAndDay(day, month, year, (long)m.getId());
            map.put(startTime, salesValue);

        }
        return map;
    }

    public Map<LocalDateTime, Integer> getSoldTicketsValue(LocalDateTime startTime, LocalDateTime endTime){
        Map<LocalDateTime, Integer> map=new TreeMap<>();
        for(;startTime.isBefore(endTime);startTime = startTime.plusDays(1)){
            int year= startTime.getYear();
            int month= startTime.getMonthValue();
            int day= startTime.getDayOfMonth();
            int salesValue=salesRepository.getSoldTicketsValueForDay(day, month, year);
            map.put(startTime, salesValue);
        }
        return map;
    }

    public Map<LocalDateTime, Integer> getSoldTickets(LocalDateTime startTime, LocalDateTime endTime){
        Map<LocalDateTime, Integer> map=new TreeMap<>();
        for(;startTime.isBefore(endTime);startTime = startTime.plusDays(1)){
            int year= startTime.getYear();
            int month= startTime.getMonthValue();
            int day= startTime.getDayOfMonth();
            int salesValue=salesRepository.getSoldTicketsForDay(day, month, year);
            map.put(startTime, salesValue);
        }
        return map;
    }


    private ObservableList<Sales> sales;

    public void fetchSales() {
        this.sales = FXCollections.observableArrayList(salesRepository.findAll());
    }

    public ObservableList<Sales> getSales() {
        if (sales == null) {
            fetchSales();
        }
        return sales;
    }

    public void addSales(Sales sale) {
        salesRepository.save(sale);
        sales.add(sale); // add to observable list, but we are not fetching from database again
    }

    @Transactional
    public boolean deleteSale(Sales sale) {

        salesRepository.delete(sale);
        sales.remove(sale);  // remove from observable list, but we are not fetching from database again
        return true;
    }

    public void updateSale(Sales sale) {
        salesRepository.save(sale);
    }
}
