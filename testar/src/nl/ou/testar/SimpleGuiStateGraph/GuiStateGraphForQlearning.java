package nl.ou.testar.SimpleGuiStateGraph;

import org.fruit.alayer.Action;
import org.fruit.alayer.State;
import org.fruit.alayer.Tags;

import java.util.*;

public class GuiStateGraphForQlearning {
    protected Set<QlearningGuiState> qlearningGuiStates;
    protected String startingStateConcreteId;
    protected String previousStateConcreteId;
    protected String previousActionConcreteId;
    private double R_MAX;
    private double gammaDiscount;

    public GuiStateGraphForQlearning(double R_MAX, double gammaDiscount) {
        this.R_MAX = R_MAX;
        this.gammaDiscount=gammaDiscount;

        qlearningGuiStates = new HashSet<QlearningGuiState>();
    }

    public Set<String> getConcreteIdsOfUnvisitedActions(State state){
        return getStateByConcreteId(state.get(Tags.ConcreteID)).getUnvisitedActionIds();
    }

    /**
     * returns null if action with given ID is not found
     *
     * @param actions
     * @param concreteActionId
     * @return
     */
    protected Action getActionWithConcreteId(Set<Action> actions, String concreteActionId){
        for(Action action:actions){
            // find the action with concreteId:
            if(action.get(Tags.ConcreteID).equals(concreteActionId)){
                return action;
            }
        }
        return null;
    }

    /**
     * Returns null if not found
     *
     * @param concreteStateId
     * @return
     */
    protected QlearningGuiState getStateByConcreteId(String concreteStateId){
        for(QlearningGuiState state: qlearningGuiStates){
            if(state.getConcreteStateId().equals(concreteStateId)){
                return state;
            }
        }
        return null;
    }

    protected QlearningGuiState createQlearningGuiState(State state, Set<Action> actions){
        HashMap<String, Double> actionIds = new HashMap<String, Double>();
        for(Action action:actions){
            actionIds.put(action.get(Tags.ConcreteID), R_MAX);
        }
        return new QlearningGuiState(state.get(Tags.ConcreteID),actionIds);
    }

    protected boolean containsStateId(String stateId){
        for(QlearningGuiState state: qlearningGuiStates){
            if(state.getConcreteStateId().equals(stateId)){
                return true;
            }
        }
        return false;
    }

}
