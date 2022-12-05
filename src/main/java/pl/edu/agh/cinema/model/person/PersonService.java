package pl.edu.agh.cinema.model.person;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private ObservableList<Person> persons;

    public void fetchPersons() {
        this.persons = FXCollections.observableArrayList(personRepository.findAll());
    }

    public ObservableList<Person> getPersons() {
        if (persons == null) {
            fetchPersons();
        }
        return persons;
    }

    public void addPerson(Person person) {
        personRepository.save(person);
        persons.add(person); // add to observable list, but we are not fetching from database again
    }

    public void updatePerson(Person person) {
        personRepository.save(person);
    }

    public void deletePerson(Person person) {
        personRepository.delete(person);
        persons.remove(person); // remove from observable list, but we are not fetching from database again
    }
}
