package com.chemistry.calculator.features.solving;

interface ResultCallback {

  void success(String result);
  void error(String message);
}
