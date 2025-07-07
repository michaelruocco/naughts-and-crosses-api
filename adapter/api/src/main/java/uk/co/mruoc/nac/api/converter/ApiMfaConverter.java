package uk.co.mruoc.nac.api.converter;

import uk.co.mruoc.nac.api.dto.ApiMfaSetting;
import uk.co.mruoc.nac.api.dto.ApiMfaSettings;
import uk.co.mruoc.nac.api.dto.ApiVerifySoftwareTokenRequest;
import uk.co.mruoc.nac.entities.MfaPreferences;
import uk.co.mruoc.nac.entities.MfaSetting;
import uk.co.mruoc.nac.entities.MfaSettings;
import uk.co.mruoc.nac.entities.VerifySoftwareTokenRequest;

public class ApiMfaConverter {

  public VerifySoftwareTokenRequest toRequest(ApiVerifySoftwareTokenRequest apiRequest) {
    return VerifySoftwareTokenRequest.builder().userCode(apiRequest.getUserCode()).build();
  }

  public MfaPreferences toPreferences(String username, ApiMfaSettings apiSettings) {
    return MfaPreferences.builder().username(username).settings(toSettings(apiSettings)).build();
  }

  public ApiMfaSettings toApiSettings(MfaSettings settings) {
    return ApiMfaSettings.builder()
        .softwareToken(toApiSetting(settings.getSoftwareToken()))
        .build();
  }

  private MfaSettings toSettings(ApiMfaSettings apiSettings) {
    return MfaSettings.builder().softwareToken(toSetting(apiSettings.getSoftwareToken())).build();
  }

  private MfaSetting toSetting(ApiMfaSetting apiSetting) {
    return MfaSetting.builder()
        .enabled(apiSetting.isEnabled())
        .preferred(apiSetting.isPreferred())
        .build();
  }

  private ApiMfaSetting toApiSetting(MfaSetting setting) {
    return ApiMfaSetting.builder()
        .enabled(setting.isEnabled())
        .preferred(setting.isPreferred())
        .build();
  }
}
