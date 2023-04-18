package guru.springframework.recipe.app.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import guru.springframework.recipe.app.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.app.converters.fromdomain.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.recipe.app.repositories.UnitOfMeasureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

	private final UnitOfMeasureRepository unitOfMeasureRepository;
	
	private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
	
	@Override
	public Set<UnitOfMeasureCommand> recupererToutesLesUnitesDeMesure() {

		Set<UnitOfMeasureCommand> toutesLesUnitesDeMesure = StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(), false)
																		.map(unitOfMeasureToUnitOfMeasureCommand::convert)
																		.collect(Collectors.toSet());
		log.info("toutesLesUnitesDeMesure.size() : " + toutesLesUnitesDeMesure.size());

		return toutesLesUnitesDeMesure;
	}

}
