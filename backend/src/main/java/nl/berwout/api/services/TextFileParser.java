package nl.berwout.api.services;

import nl.berwout.api.exceptions.InvalidFileFormatException;
import nl.berwout.api.models.CourseInstance;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Service
public class TextFileParser {
    private final Predicate<String> titel = input -> input.startsWith("Titel: ");
    private final Predicate<String> cursusCode = input -> input.startsWith("Cursuscode: ");
    private final Predicate<String> duur = input -> input.matches("Duur: [12345] dagen\\s?");
    private final Predicate<String> startDatum = input -> input.matches("Startdatum: \\d\\d?/\\d\\d?/\\d{4}\\s?");
    private final Predicate<String> newLine = input -> input.trim().isEmpty();

    private List<Predicate<String>> predicateList;

    //ideally we would want to hand this parser a list of predicates,
    // so it can be used in a more generic way, but right now it only has one use so it's fine.
    public TextFileParser(){
        this.predicateList = new ArrayList<>();
        this.predicateList.add(titel);
        this.predicateList.add(cursusCode);
        this.predicateList.add(duur);
        this.predicateList.add(startDatum);
        this.predicateList.add(newLine);
    }


    public List<CourseInstance> parse(String toParse) throws InvalidFileFormatException{
        ArrayList<CourseInstance> result = new ArrayList<>();
        if(fileIsValid(toParse)){
            //eliminate newlines from file and split on instances
            List<String> courseInstances = splitInstances(toParse);
            for(String courseInstance: courseInstances){
                //split instances into properties
                String[] properties = courseInstance.split("\\n");
                String title = properties[0].replace("Titel: ", "");
                String courseCode = properties[1].replace("Cursuscode: ", "");
                byte duration = Byte.parseByte(properties[2].replaceAll("[^\\d]", ""));
                Date startDate = convertStringToDate(properties[3].replace("Startdatum: ", ""));
                result.add(new CourseInstance(title, courseCode, duration, startDate));
            }
        }
        return result;
    }

    //this function is used to split on newlines with varying newline syntax (\n\r, \n or \r, doesn't matter).
    private List<String> splitInstances(String toSplit){
        List<String> result = new ArrayList<>();
        String[] lines = toSplit.split(System.getProperty("line.separator"));
        StringBuilder instance = new StringBuilder();
        for(String line: lines){
            if(line.trim().isEmpty()){
                result.add(instance.toString());
                instance = new StringBuilder();
            } else{
                instance.append(line + "\n");
            }
        }
        if(!instance.toString().isEmpty()){
            result.add(instance.toString());
        }
        return result;
    }

    private boolean fileIsValid(String toVerify) throws InvalidFileFormatException {
        String[] result = toVerify.split("\\n");
        int currentPredicate = 0;
        for(int i = 0; i < result.length; i++){
            if(!predicateList.get(currentPredicate).test(result[i])){
                throw new InvalidFileFormatException("Bestand is niet in correct formaat op regel " + i + "."
                        + "\nEr zijn geen cursusinstanties toegevoegd.");
            }
            //loop through the predicates, starting from the top if we reach the end.
            currentPredicate = currentPredicate < this.predicateList.size() - 1 ? currentPredicate + 1 : 0;
        }
        return true;
    }

    private Date convertStringToDate(String date){
        Date result = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            result = sdf.parse(date);
        } catch (ParseException ex){
            System.out.println(ex.getMessage());
        }
        return result;
    }
}
