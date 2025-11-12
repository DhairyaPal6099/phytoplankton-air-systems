package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.dashboard;

public interface DashboardView {
    void showAqi(int aqi);
    void showLight(double light);
    void showTurbidity(double turbidity);
    void showProximity(boolean proximity);

    void showCarbonDiOxide(double co2);

    void showError(String message);
}
