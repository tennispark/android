package com.luckydut97.tennispark.feature_auth.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.luckydut97.tennispark.core.ui.components.button.TennisParkButton
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(354.dp)
                .height(400.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "개인정보 처리방침",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontFamily = Pretendard
                )
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    val titleStyle = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF262626),
                        fontFamily = Pretendard,
                        lineHeight = 24.sp
                    )
                    val sectionTitleStyle = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF262626),
                        fontFamily = Pretendard,
                        lineHeight = 22.sp
                    )
                    val contentStyle = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFF262626),
                        fontFamily = Pretendard,
                        lineHeight = 20.sp
                    )

                    // 첫 번째 섹션: 개인정보 수집 및 이용 동의서
                    Text("개인정보 수집 및 이용 동의서_테니스파크", style = titleStyle)
                    Spacer(Modifier.height(12.dp))

                    Text(
                        "(테니스파크)(이하 \"회사\")는 \"개인정보 보호법」 등 관련 법령에 따라 이용자의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 다음과 같은 개인정보처리방침을 수립·공개합니다.본 방침은 TennisPark 서비스에 적용됩니다.",
                        style = contentStyle
                    )
                    Spacer(Modifier.height(16.dp))

                    Text("1. 개인정보 수집 항목 및 수집 방법", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("회사는 아래와 같은 개인정보를 수집합니다.", style = contentStyle)
                    Spacer(Modifier.height(8.dp))

                    // 표 1
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE0E0E0))
                    ) {
                        // 헤더
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5))
                                .padding(8.dp)
                        ) {
                            Text("구분", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Text("수집 항목", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(2f))
                            Text("수집 방법", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(2f))
                        }
                        Divider(color = Color(0xFFE0E0E0))
                        // 필수 행
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("필수", fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Text("이름, 전화번호, 성별, 구력, 나이, 가입경로, 인스타그램 ID", fontSize = 12.sp, modifier = Modifier.weight(2f))
                            Text("회원가입 및 서비스 이용 시 입력을 통한 수집", fontSize = 12.sp, modifier = Modifier.weight(2f))
                        }
                        Divider(color = Color(0xFFE0E0E0))
                        // 자동 수집 행
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("자동\n수집", fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Text("접속 로그, 기기 정보(OS, 모델명 등), 서비스 이용기록", fontSize = 12.sp, modifier = Modifier.weight(2f))
                            Text("서비스 이용 중 자동 수집 또는 분석 도구(Google Analytics 등)를 통한 수집", fontSize = 12.sp, modifier = Modifier.weight(2f))
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text("2. 개인정보 수집 및 이용 목적", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("회사는 수집한 개인정보를 다음의 목적을 위해 사용합니다.", style = contentStyle)
                    Spacer(Modifier.height(4.dp))
                    Text("• 회원 식별 및 본인 확인", style = contentStyle)
                    Text("• 테니스 관련 모임 및 활동 정보 제공", style = contentStyle)
                    Text("• 맞춤형 모임 추천, 이벤트 안내, 커뮤니티 운영", style = contentStyle)
                    Text("• 서비스 이용에 따른 문의 응대, 공지사항 전달", style = contentStyle)
                    Text("• 서비스 품질 향상을 위한 통계 분석 및 운영 최적화", style = contentStyle)
                    Text("• 비정상적 이용 방지를 위한 보안 관리", style = contentStyle)

                    Spacer(Modifier.height(16.dp))

                    Text("3. 개인정보 처리 및 보유 기간", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("회사는 개인정보를 수집·이용 목적을 위한 범위에서 보유하며, 다음의 경우에 한해 기재된 기간 동안 보유합니다.", style = contentStyle)
                    Spacer(Modifier.height(4.dp))
                    Text("• 회원 탈퇴 또는 서비스 종료 시: 지체 없이 파기", style = contentStyle)
                    Text("• 관련 법령에 따라 보존이 필요한 경우: 해당 법령에서 정한 기간", style = contentStyle)

                    Spacer(Modifier.height(16.dp))

                    Text("4. 개인정보 제3자 제공", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("회사는 이용자의 동의 없이 개인정보를 제3자에게 제공하지 않습니다. 단, 다음의 경우는 예외로 합니다.", style = contentStyle)
                    Spacer(Modifier.height(4.dp))
                    Text("• 법령에 근거한 경우", style = contentStyle)
                    Text("• 수사기관의 요청 등 법적 의무 이행을 위한 경우", style = contentStyle)

                    Spacer(Modifier.height(16.dp))

                    Text("5. 개인정보의 자동 수집 항목 및 거부 방법", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("회사는 이용자의 편의 및 서비스 품질 향상을 위해 다음과 같은 정보를 자동으로 수집합니다.", style = contentStyle)
                    Spacer(Modifier.height(4.dp))
                    Text("• 수집 항목: 접속 일시, 기기 정보(OS, 모델명 등), 이용 로그, 오류 로그, 서비스 이용기록", style = contentStyle)
                    Text("• 수집 목적: 서비스 이용 분석, 오류 개선, 맞춤 서비스 제공, 보안 강화", style = contentStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("자동 수집 거부 방법:", style = contentStyle)
                    Text("• 앱 이용 시 설정 > 개인정보 메뉴에서 일부 정보 수집을 차단할 수 있습니다. 단, 일부 기능은 제한될 수 있습니다.", style = contentStyle)

                    Spacer(Modifier.height(16.dp))

                    Text("6. 개인정보 파기 절차 및 방법", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("개인정보는 보유기간 경과 또는 처리 목적 달성 후 즉시 파기합니다.", style = contentStyle)
                    Spacer(Modifier.height(4.dp))
                    Text("• 전자적 파일: 복구 불가능한 방식으로 영구 삭제", style = contentStyle)
                    Text("• 종이 문서: 분쇄기로 분쇄 또는 소각", style = contentStyle)

                    Spacer(Modifier.height(16.dp))

                    Text("7. 정보주체의 권리와 행사 방법", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("이용자는 언제든지 본인의 개인정보에 대해 다음과 같은 권리를 행사할 수 있습니다.", style = contentStyle)
                    Spacer(Modifier.height(4.dp))
                    Text("• 개인정보 열람, 수정, 삭제 요청", style = contentStyle)
                    Text("• 처리 정지 요구", style = contentStyle)
                    Text("• 개인정보 수집 및 이용 동의 철회", style = contentStyle)
                    Spacer(Modifier.height(4.dp))
                    Text("※ 권리는 앱 내 설정 또는 고객센터를 통해 행사할 수 있습니다.", style = contentStyle)

                    Spacer(Modifier.height(16.dp))

                    Text("8. 개인정보 보호를 위한 안전성 확보 조치", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("회사는 개인정보 보호를 위해 다음과 같은 조치를 취하고 있습니다.", style = contentStyle)
                    Spacer(Modifier.height(4.dp))
                    Text("• 개인정보 접근 권한 최소화", style = contentStyle)
                    Text("• 암호화 및 보안 서버 운영", style = contentStyle)
                    Text("• 해킹 및 바이러스 대응 시스템 구축", style = contentStyle)
                    Text("• 개인정보 취급자에 대한 정기 교육", style = contentStyle)

                    Spacer(Modifier.height(16.dp))

                    Text("9. 개인정보 보호책임자", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text("이용자는 개인정보 관련 문의 및 민원처리를 아래로 요청할 수 있습니다.", style = contentStyle)
                    Spacer(Modifier.height(8.dp))

                    // 표 2
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE0E0E0))
                    ) {
                        // 개인정보 보호책임자
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("개인정보 보호책임자", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text("김성웅", fontSize = 12.sp, modifier = Modifier.weight(2f))
                        }
                        Divider(color = Color(0xFFE0E0E0))
                        // 연락처
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("연락처", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text("(010-4772-0580 /\nceo@tennispark.co.kr)", fontSize = 12.sp, modifier = Modifier.weight(2f))
                        }
                        Divider(color = Color(0xFFE0E0E0))
                        // 문의 시간
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("문의 시간", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text("평일 10:00 ~ 18:00 (주말·공휴일 제외)", fontSize = 12.sp, modifier = Modifier.weight(2f))
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Text("• 개인정보침해신고센터: https://privacy.kisa.or.kr / 국번 없이 118", style = contentStyle)
                    Text("• 개인정보분쟁조정위원회: https://www.kopico.go.kr / 1833-6972", style = contentStyle)
                    Text("• 사이버범죄수사과: https://www.police.go.kr / 국번 없이 182", style = contentStyle)

                    Spacer(Modifier.height(16.dp))

                    Text("10. 개인정보처리방침의 변경", style = sectionTitleStyle)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "본 개인정보처리방침은 관련 법령 및 내부 정책에 따라 변경될 수 있으며, 변경 시 앱 또는 홈페이지를 통해 사전 공지합니다.",
                        style = contentStyle
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("• 시행일자: 2025년 7월 1일", style = contentStyle)
                }
                Spacer(modifier = Modifier.height(24.dp))
                TennisParkButton(
                    text = "확인",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}