package rapidMiner;

import com.rapidminer.Process;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.repository.*;
import com.rapidminer.repository.local.LocalRepository;
import com.rapidminer.tools.XMLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by radek on 3.4.17.
 */
public class RapidMiner {

    private static final Logger LOG = LoggerFactory.getLogger(RapidMiner.class);

    private IOContainer output;
    private List<ApplyModelResult> results = new ArrayList<ApplyModelResult>();

    private void runProcessApplyModel () throws XMLException, InstantiationException, IllegalAccessException, IOException, OperatorException, RepositoryException {
        com.rapidminer.RapidMiner.setExecutionMode(com.rapidminer.RapidMiner.ExecutionMode.COMMAND_LINE);
        com.rapidminer.RapidMiner.init();

        RepositoryManager repositoryManager = RepositoryManager.getInstance(new RepositoryAccessor() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });

        File root = new File("repository/");

        LocalRepository localRepository = new LocalRepository("Repository", root);
        repositoryManager.addRepository(localRepository);

        repositoryManager.save();


        File file = new File("repository/process.rmp");
        Process pr = new Process(file);
        pr.getOperator("Read CSV").setParameter("data_set_meta_data_information", "<parameter key=\"50\" value=\"att50.true.integer.attribute\"/>");

        IOContainer input = new IOContainer();
        LOG.trace("RapidMiner procces run");
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

    public List<ApplyModelResult> run() throws XMLException, InstantiationException, OperatorException, IOException, IllegalAccessException, RepositoryException {
        runProcessApplyModel();
        setResult();
        return results;
    }
}



