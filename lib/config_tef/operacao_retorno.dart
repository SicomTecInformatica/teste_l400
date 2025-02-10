/// Classe de retorno, utilizada para atribuir o resultado do Tef Ger7 para um Json.
class RetornoGer7 {
  String? _version = '';
  String? _status = '';
  String? _config = '';
  String? _license = '';
  String? _terminal = '';
  String? _merchant = '';
  String? _id = '';
  String? _type = '';
  String? _product = '';
  String? _response = '';
  String? _authorization = '';
  String? _amount = '';
  String? _installments = '';
  String? _instmode = '';
  String? _stan = '';
  String? _rrn = '';
  String? _time = '';
  String? _print = '';
  String? _track2 = '';
  String? _aid = '';
  String? _cardholder = '';
  String? _prefname = '';
  String? _errcode = '';
  String? _errmsg = '';
  String? _label = '';

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['version'] = getVersion;
    data['status'] = getStatus;
    data['config'] = getConfig;
    data['license'] = getLicens;
    data['terminal'] = getTerminal;
    data['merchant'] = getMerchant;
    data['id'] = getId;
    data['type'] = getType;
    data['product'] = getProduct;
    data['response'] = getResponse;
    data['authorizationType'] = getAuthorization;
    data['amount'] = getAmount;
    data['installments'] = getInstallments;
    data['instmode'] = getInstmode;
    data['stan'] = getStan;
    data['rrn'] = getRrn;
    data['time'] = getTime;
    data['print'] = getPrint;
    data['track2'] = getTrack2;
    data['aid'] = getAid;
    data['cardholder'] = getCardholder;
    data['prefname'] = getPrefname;
    data['errcode'] = getErrcode;
    data['errmsg'] = getErrmsg;
    data['label'] = getLabel;
    return data;
  }

  RetornoGer7.fromJson(Map<String, dynamic> json) {
    _version = json['version'] ?? '';
    _status = json['status'] ?? '';
    _config = json['config'] ?? '';
    _license = json['license'] ?? '';
    _terminal = json['terminal'] ?? '';
    _merchant = json['merchant'] ?? '';
    _id = json['id'] ?? '';
    _type = json['type'] ?? '';
    _product = json['product'] ?? '';
    _response = json['response'] ?? '';
    _authorization = json['authorizationType'] ?? '';
    _amount = json['amount'] ?? '';
    _installments = json['installments'] ?? '';
    _instmode = json['instmode'] ?? '';
    _stan = json['stan'] ?? '';
    _rrn = json['rrn'] ?? '';
    _time = json['time'] ?? '';
    _print = json['print'] ?? '';
    _track2 = json['track2'] ?? '';
    _aid = json['aid'] ?? '';
    _cardholder = json['cardholder'] ?? '';
    _prefname = json['prefname'] ?? '';
    _errcode = json['errcode'] ?? '';
    _errmsg = json['errmsg'] ?? '';
    _label = json['label'] ?? '';
  }

  String get getVersion {
    return _version ?? "";
  }

  set setVersion(String version) {
    _version = version;
  }

  String get getStatus {
    return _status ?? "";
  }

  set setStatus(String status) {
    _status = status;
  }

  String get getConfig {
    return _config ?? "";
  }

  set setConfig(String config) {
    _config = config;
  }

  String get getLicens {
    return _license ?? "";
  }

  set setLicense(String license) {
    _license = license;
  }

  String get getTerminal {
    return _terminal ?? "";
  }

  set setTerminal(String terminal) {
    _terminal = terminal;
  }

  String get getMerchant {
    return _merchant ?? "";
  }

  set setMerchant(String merchant) {
    _merchant = merchant;
  }

  String get getId {
    return _id ?? "";
  }

  set setId(String id) {
    _id = id;
  }

  String get getType {
    return _type ?? "";
  }

  set setType(String type) {
    _type = type;
  }

  String get getProduct {
    return _product ?? "";
  }

  set setProduct(String product) {
    _product = product;
  }

  String get getResponse {
    return _response ?? "";
  }

  set setResponse(String response) {
    _response = response;
  }

  String get getAuthorization {
    return _authorization ?? "";
  }

  set setAuthorization(String authorization) {
    _authorization = authorization;
  }

  String get getAmount {
    return _amount ?? "";
  }

  set setAmount(String amount) {
    _amount = amount;
  }

  String get getInstallments {
    return _installments ?? "";
  }

  set setInstallments(String installments) {
    _installments = installments;
  }

  String get getInstmode {
    return _instmode ?? "";
  }

  set setInstmode(String instmode) {
    _instmode = instmode;
  }

  String get getStan {
    return _stan ?? "";
  }

  set setStan(String stan) {
    _stan = stan;
  }

  String get getRrn {
    return _rrn ?? "";
  }

  set setRrn(String rrn) {
    _rrn = rrn;
  }

  String get getTime {
    return _time ?? "";
  }

  set setTime(String time) {
    _time = time;
  }

  String get getPrint {
    return _print ?? "";
  }

  set setPrint(String print) {
    _print = print;
  }

  String get getTrack2 {
    return _track2 ?? "";
  }

  set setTrack2(String track2) {
    _track2 = track2;
  }

  String get getAid {
    return _aid ?? "";
  }

  set setAid(String aid) {
    _aid = aid;
  }

  String get getCardholder {
    return _cardholder ?? "";
  }

  set setCardholder(String cardholder) {
    _cardholder = cardholder;
  }

  String get getPrefname {
    return _prefname ?? "";
  }

  set setPrefname(String prefname) {
    _prefname = prefname;
  }

  String get getErrcode {
    return _errcode ?? "";
  }

  set setErrcode(String errcode) {
    _errcode = errcode;
  }

  String get getErrmsg {
    return _errmsg ?? "";
  }

  set setErrmsg(String errmsg) {
    _errmsg = errmsg;
  }

  String get getLabel {
    return _label ?? "";
  }

  set setLabel(String label) {
    _label = label;
  }
}

/// Classe de retorno, utilizada para atribuir o resultado do M-Sitef para um Json.
class RetornoMsiTef {
  String? _cODRESP = '';
  String? _cOMPDADOSCONF = '';
  String? _cODTRANS = '';
  String? _vLTROCO = '';
  String? _rEDEAUT = '';
  String? _bANDEIRA = '';
  String? _nSUSITEF = '';
  String? _nSUHOST = '';
  String? _cODAUTORIZACAO = '';
  String? _tipoPARC = '';
  String? _numPARC = '';
  String? _viaESTABELECIMENTO = '';
  String? _viaCLIENTE = '';

  RetornoMsiTef.fromJson(Map<String, dynamic> json) {
    _cODRESP = json['CODRESP'] ?? '';
    _cOMPDADOSCONF = json['COMP_DADOS_CONF'] ?? '';
    _cODTRANS = json['CODTRANS'] ?? '';
    _vLTROCO = json['VLTROCO'] ?? '';
    _rEDEAUT = json['REDE_AUT'] ?? '';
    _bANDEIRA = json['BANDEIRA'] ?? '';
    _nSUSITEF = json['NSU_SITEF'] ?? '';
    _nSUHOST = json['NSU_HOST'] ?? '';
    _cODAUTORIZACAO = json['COD_AUTORIZACAO'] ?? '';
    _tipoPARC = json['TIPO_PARC'] ?? '';
    _numPARC = json['NUM_PARC'] ?? '';
    _viaCLIENTE = json['VIA_CLIENTE'] ?? '';
    _viaESTABELECIMENTO = json['VIA_ESTABELECIMENTO'] ?? '';
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CODRESP'] = _cODRESP;
    data['COMP_DADOS_CONF'] = _cOMPDADOSCONF;
    data['CODTRANS'] = _cODTRANS;
    data['VLTROCO'] = _vLTROCO;
    data['REDE_AUT'] = _rEDEAUT;
    data['BANDEIRA'] = _bANDEIRA;
    data['NSU_SITEF'] = _nSUSITEF;
    data['NSU_HOST'] = _nSUHOST;
    data['COD_AUTORIZACAO'] = _cODAUTORIZACAO;
    data['TIPO_PARC'] = _tipoPARC;
    data['NUM_PARC'] = _numPARC;
    data['VIA_ESTABELECIMENTO'] = _viaESTABELECIMENTO;
    data['VIA_CLIENTE'] = _viaCLIENTE;
    return data;
  }

  get getNSUHOST => _nSUHOST;
  String? get getSitefTipoParcela => _tipoPARC;
  get getNSUSitef => _nSUSITEF;
  get getCodTrans => _cODTRANS;
  get getNameTransCod {
    String retorno = "Valor invalido";
    switch (int.parse(_tipoPARC??"0")) {
      case 0:
        retorno = "A vista";
        break;
      case 1:
        retorno = "Pré-Datado";
        break;
      case 2:
        retorno = "Parcelado Loja";
        break;
      case 3:
        retorno = "Parcelado Adm";
        break;
    }
    return retorno;
  }

  get getvlTroco => _vLTROCO;
  get getParcelas {
    return _numPARC??"";
  }

  get getCodAutorizacao => _cODAUTORIZACAO ?? "";
  String get textoImpressoEstabelecimento => _viaESTABELECIMENTO ?? "";
  String get textoImpressoCliente => _viaCLIENTE ?? "";
  get getCompDadosConf => _cOMPDADOSCONF ?? "";
  get getCodResp => _cODRESP ?? "";
  get getRedeAut => _rEDEAUT ?? "";
  get getBandeira => _bANDEIRA ?? "";
}
