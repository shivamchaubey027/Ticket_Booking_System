package ticket_booking.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket_booking.Entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_DB_PATH = "app/src/main/java/ticket_booking/localDb/trains.json";

    public TrainService() throws IOException {
        File trains = new File(TRAIN_DB_PATH);

        if (trains.exists()) {
            try {
                byte[] fileBytes = java.nio.file.Files.readAllBytes(trains.toPath());
            } catch (Exception e) {
                System.out.println("Error reading file content: " + e.getMessage());
            }
        }

        try {
            trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
        } catch (Exception e) {
            System.out.println("Error parsing trains: " + e.getMessage());
            e.printStackTrace();
            trainList = new ArrayList<>();
        }
    }


    public List<Train> searchTrains(String source, String destination) {


        List<Train> results = new ArrayList<>();

        for (Train train : trainList) {


            boolean isValid = validTrain(train, source, destination);


            if (isValid) {
                results.add(train);
            }
        }

        return results;
    }

    public void addTrain(Train newTrain) {
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            updateTrain(newTrain);
        } else {
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            addTrain(updatedTrain);
        }
    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();
        if (stations == null || stations.isEmpty()) {
            return false;
        }

        String lowerSource = source.toLowerCase();
        String lowerDest = destination.toLowerCase();

        int sourceIndex = -1;
        int destIndex = -1;

        for (int i = 0; i < stations.size(); i++) {
            String station = stations.get(i).toLowerCase();
            if (station.equals(lowerSource)) {
                sourceIndex = i;
            }
            if (station.equals(lowerDest)) {
                destIndex = i;
            }
        }

        return (sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex);
    }



}