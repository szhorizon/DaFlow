package com.abhioncbr.etlFramework.commons.common.query

import com.abhioncbr.etlFramework.commons.common.GeneralParam

case class QueryObject(queryFile: QueryFilesParam, queryArgs: Option[Array[GeneralParam]])