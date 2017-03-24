package uniud.trecevaloo.relevance;

import uniud.trecevaloo.exceptions.TrecEvalOORelevanceException;

/**
 * This class  manages any binary relevance type. All it needs is to define the relevant symbol and the not relevant
 * symbol in the constructor.
 */
public class BinaryRelevanceType implements RelevanceType {

    static final int RELEVANT = 1;
    static final int NOT_RELEVANT = 0;

    /**
     * The BinaryRelevanceType constructor.
     */
    public BinaryRelevanceType(String relevant, String notRelevant){}

    /**
     * It reads correctly the symbol and gives the value used inside the system.
     * Is used by the system to read correctly a relevance when you are importing run and qrels.
     *
     * @param value the value to read (Can be anything).
     * @return the value used in the system.
     */
    @Override
    public double readValue(Object value) {

        if(value.equals(NOT_RELEVANT) ){
            return 0;
        } else if (value.equals(RELEVANT)){
            return 1;
        } else {
            throw new TrecEvalOORelevanceException("unreadable binary relevance " + value);
        }
    }

    /**
     * It defines if a relevance judgment is relevant or not. (Used by the system).
     * @param value the value.
     * @return true if it is relevant, otherwise false.
     */
    @Override
    public boolean isRelevant(double value) {
        return (value == 1);
    }

    /**
     * It defines if a relevance is unjudged.
     * @param value the value.
     * @return true if it is unjudged, otherwise false.
     */
    @Override
    public boolean isUnjudged(double value) {
        return false;
    }


}
