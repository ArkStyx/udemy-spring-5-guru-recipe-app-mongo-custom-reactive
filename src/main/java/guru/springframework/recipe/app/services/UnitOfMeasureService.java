package guru.springframework.recipe.app.services;

import java.util.Set;

import guru.springframework.recipe.app.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {

	Set<UnitOfMeasureCommand> recupererToutesLesUnitesDeMesure();
	
}
