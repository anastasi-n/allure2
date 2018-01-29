package io.qameta.allure.entity;

/**
 * @author Dmitry Baev baev@qameta.io
 *         Date: 31.01.16
 */
public interface WithStatistic {

    Statistic getStatistic();

    void setStatistic(Statistic statistic);

    default void updateStatistic(Statistic other) {
        getStatistic().setFailed(other.getFailed() + getStatistic().getFailed());
        getStatistic().setBroken(other.getBroken() + getStatistic().getBroken());
        getStatistic().setPassed(other.getPassed() + getStatistic().getPassed());
        getStatistic().setKnownissuesonly(other.getKnownissuesonly() + getStatistic().getKnownissuesonly());
        getStatistic().setSkipped(other.getSkipped() + getStatistic().getSkipped());
        getStatistic().setPending(other.getPending() + getStatistic().getPending());
        getStatistic().setNotcovered(other.getNotcovered() + getStatistic().getNotcovered());
        getStatistic().setUnknown(other.getUnknown() + getStatistic().getUnknown());
    }

    default void updateStatistic(Statusable statusable) {
        if (statusable == null) {
            return;
        }
        if (getStatistic() == null) {
            setStatistic(new Statistic());
        }
        getStatistic().update(statusable.getStatus());
    }
}
