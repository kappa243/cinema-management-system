# Cinema

> Poniższa dokumentacja jest oparta na ostatnim pełnym stanie projektu **M1**.

# Struktura projektu

Funkcjonalności projektu zostały podzielone na następujące grupy:
- część głównej aplikacji - zarządzająca uruchomieniem aplikacji oraz inicjalizacją Spring'a i JavaFX'a
- część UI - odowiedzialna za zarządzanie widokiem, prezentacją danych i komunikacją z użytkownikiem
- część modeli danych - tworzy modele bazodanowe, zapewnia komunikację z bazą danych poprzez API i udostępnia serwisy
  do swoich modeli

## Główna aplikacja

Jej głównym zadaniem jest inicjalizacja JavaFX i Spring, z odpowiednimi ustawieniami. Proces ten zaczyna się w *CinemaApplication.java*. Uruchamia on event odpowiedzialny za utworzonie głównego okna aplikacji oraz rejestruje beany dające dostęp do aplikacji.

*StageManager.java* jest rdzeniem aplikacji, który może uruchomić i zamknąć program z dowolnego miejsca aplikacji (rejestruje EventListener'y, które odbierają eventy za to odpowiedzialne; np. *CinemaApplication.java* wywołuje event uruchomienia) oraz zapewnia dostęp do głównego widoku.

Zadaniem *ViewManager.java* jest załadowanie widoku FXML i utworzenie controllerów odpowiedzialnych za nie poprzez Spring'a. Dzięki temu obiekty w controllerach zostaną wstrzyknięte przez Spring'a.


## UI

Zawiera controllery widoków.

### *ApplicationController*

Odpowiada za główne okno aplikacji, w którym znajduje się lista osób oraz guziki do zarządzania nimi. Mogą one tworzyć, edytować i usuwać użytkowników. W przypadku tworzenia i edycji otwierany jest osobne okno, w którym można wprowadzić dane.

### *EditUserController*

Okno edycji użytkownika, w którym można zmienić jego informacje. Jednocześnie jest wykorzystywane jako okno tworzenia nowego użytkownika.


## Modele danych
