package org.fastorm.stats;

public interface IProfilable<Situation, Context> {

	Context start(Situation situation) throws Exception;

	void job(Situation situation, Context context) throws Exception;

	void finish(Situation situation, Context context) throws Exception;
}
