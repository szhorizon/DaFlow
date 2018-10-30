package com.abhioncbr.etlFramework.etl_feed.transformData

import com.abhioncbr.etlFramework.commons.transform.Transform
import com.abhioncbr.etlFramework.commons.transform.TransformationResult
import com.typesafe.scalalogging.Logger
import org.apache.spark.sql.DataFrame

class TransformData(transform : Transform) {
  private val logger = Logger(this.getClass)

  def performTransformation(rawDataFrame: DataFrame): Either[Array[TransformationResult], String] = {
    val steps = transform.transformationSteps
    var stepOutput: Array[TransformationResult] = Array(TransformationResult(rawDataFrame, null, null))

    //iterating over transformation steps
    steps.foreach(step => {
      //setting up input data frames in transformation step
      step.addInputData(stepOutput.map(res => res.resultDF)) match {
        //iterating for each group of transformation rules
        case Left(b) =>
          stepOutput = Array()
          step.getRules.zipWithIndex.foreach(rule => {
            logger.info(s"step order: ${step.getOrder}, rule: $rule - checking condition")
            if (rule._1._2.condition(step.getInputData)) {
              logger.info(s"step order: ${step.getOrder}, rule: $rule - executing")
              rule._1._2.execute(step.getInputData) match {
                case Left(array) => stepOutput = stepOutput ++ array
                case Right(s) => return Right(s)
              }
            } else
              return Right(s"For transformation step order: ${step.getOrder}, rule order: ${rule._1._2.getOrder}, rule group:${rule._1._2.getGroup} : condition failed.")
          })
        case Right(e) => return Right(e)
      }
    })
    Left(stepOutput)
  }

}