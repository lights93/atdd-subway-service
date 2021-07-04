package nextstep.subway.line.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class LineService {
	private LineRepository lineRepository;
	private StationService stationService;

	public LineService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	public LineResponse saveLine(LineRequest request) {
		Station upStation = stationService.findById(request.getUpStationId());
		Station downStation = stationService.findById(request.getDownStationId());
		Line persistLine = lineRepository.save(Line.of(request, upStation, downStation));
		return LineResponse.of(persistLine);
	}

	public List<LineResponse> findLines() {
		return LineResponse.of(lineRepository.findAll());
	}

	private Line findLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(RuntimeException::new);
	}

	public LineResponse findLineResponseById(Long id) {
		return LineResponse.of(findLineById(id));
	}

	public void updateLine(Long id, LineRequest lineUpdateRequest) {
		Line persistLine = findLineById(id);
		persistLine.update(Line.of(lineUpdateRequest));
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long lineId, SectionRequest request) {
		Line line = findLineById(lineId);
		Station upStation = stationService.findStationById(request.getUpStationId());
		Station downStation = stationService.findStationById(request.getDownStationId());
		line.addLineStation(new Section(line, upStation, downStation, request.getDistance()));
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station station = stationService.findStationById(stationId);
		if (line.sections().size() <= 1) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = line.sections().stream()
			.filter(it -> it.upStation() == station)
			.findFirst();
		Optional<Section> downLineStation = line.sections().stream()
			.filter(it -> it.downStation() == station)
			.findFirst();

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().upStation();
			Station newDownStation = upLineStation.get().downStation();
			int newDistance = upLineStation.get().distance() + downLineStation.get().distance();
			line.sections().add(new Section(line, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> line.sections().remove(it));
		downLineStation.ifPresent(it -> line.sections().remove(it));
	}
}
