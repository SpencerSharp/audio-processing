public class Modulator {
    private ModulationRelationship[] modulatedBy;
    int period;
    ModulatingFunction function;

    public float get(int step) {
        value = function.get(step);
        for(ModulationRelationship relationship : modulatedBy) {
            value = relationship.applyTo(step, value);
        }
        return value;
    }
}