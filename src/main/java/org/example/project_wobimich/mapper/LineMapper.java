package org.example.project_wobimich.mapper;

import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.model.Line;


public class LineMapper {

    public static Line mapToLine(RealTimeMonitorDTO realTimeMonitorDTO) {
        Line line = null;
        if (realTimeMonitorDTO != null) {
            line = new Line(realTimeMonitorDTO.getLineID(),
                    realTimeMonitorDTO.getLineName(),
                    realTimeMonitorDTO.getDirection(),
                    realTimeMonitorDTO.getTypeOfTransportation(),
                    realTimeMonitorDTO.isBarrierFree(),
                    realTimeMonitorDTO.isRealTimeSupported(),
                    realTimeMonitorDTO.getDepartureTime()
            );
        }

        return line;
    }

}
