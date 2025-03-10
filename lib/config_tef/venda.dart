/// Classe utilizada para enviar os dados da venda para o Tef Ger7.
class Venda {
  String _type = '';
  String _id = '';
  String _amount = '';
  String _installments = '';
  String _instmode = '';
  String _product = '';
  String _receipt = '';
  String _apiversion = '';

  Venda(String s, {
    String? type,
    String? id,
    String? amount,
    String? installments,
    String? instmode,
    String? product,
    String? receipt,
    String? apiversion,
  }) {
    _type = type ?? '';
    _id = id ?? '';
    _amount = amount ?? '';
    _installments = installments ?? '';
    _instmode = instmode ?? '';
    _product = product ?? '';
    _receipt = receipt ?? '';
    _apiversion = apiversion ?? '';
  }
  Map toJson() {
    Map map = {};
    map["amount"] = getAmount;
    map["apiversion"] = getApiversion;
    map["id"] = getId;
    map["installments"] = getInstallments;
    map["instmode"] = getInstmode;
    map["product"] = getProduct;
    map["receipt"] = getReceipt;
    map["type"] = getType;
    return map;
  }

  String get getType {
    return _type;
  }

  set setType(String type) {
    _type = type;
  }

  String get getId {
    return _id;
  }

  set setId(String id) {
    _id = id;
  }

  String get getAmount {
    return _amount;
  }

  set setAmount(String amount) {
    _amount = amount;
  }

  String get getInstallments {
    return _installments;
  }

  set setInstallments(String installments) {
    _installments = installments;
  }

  String get getInstmode {
    return _instmode;
  }

  set setInstmode(String instmode) {
    _instmode = instmode;
  }

  String get getProduct {
    return _product;
  }

  set setProduct(String product) {
    _product = product;
  }

  String get getReceipt {
    return _receipt;
  }

  set setReceipt(String receipt) {
    _receipt = receipt;
  }

  String get getApiversion {
    return _apiversion;
  }

  set setApiversion(String apiversion) {
    _apiversion = apiversion;
  }
}
