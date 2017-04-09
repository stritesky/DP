package rapidMiner;

import com.rapidminer.Process;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.XMLException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by radek on 3.4.17.
 */
public class RapidMiner {

    private IOContainer output;
    private List<ApplyModelResult> results = new ArrayList<ApplyModelResult>();

    private void runProcessApplyModel () throws XMLException, InstantiationException, IllegalAccessException, IOException, OperatorException {
            com.rapidminer.RapidMiner.setExecutionMode(com.rapidminer.RapidMiner.ExecutionMode.COMMAND_LINE);
            com.rapidminer.RapidMiner.init();

            ExampleSet resultSet1 = null;
//            RepositoryLocation loc = new RepositoryLocation("/home/radek/.RapidMiner/repositories/Local Repository/final.rmp");
//            Process pr = new RepositoryProcessLocation(loc).load(null);

            Process pr = com.rapidminer.RapidMiner.readProcessFile(new File("final2.rmp"));
            IOContainer input = new IOContainer();
            this.output = pr.run(input);
    }

    private void setResult () {
        ExampleSet resultSet1 = null;
        resultSet1 = (ExampleSet)output.getElementAt(0);
        ApplyModelResult applyModelResult = null;

        for (Example example : resultSet1) {
            Iterator<Attribute> allAtts = example.getAttributes().allAttributes();
            applyModelResult = new ApplyModelResult();
            while(allAtts.hasNext()) {
                Attribute a = allAtts.next();
                if (a.getName().equals(applyModelResult.ATTRIBUTE_PREDICTION)) {
                    applyModelResult.setPrediction(example.getValueAsString(a));
                }
            }
            results.add(applyModelResult);
        }
    }

    public List<ApplyModelResult> run() throws XMLException, InstantiationException, OperatorException, IOException, IllegalAccessException {
        runProcessApplyModel();
        setResult();
        return results;
    }
}



