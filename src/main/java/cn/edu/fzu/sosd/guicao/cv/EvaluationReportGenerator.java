package cn.edu.fzu.sosd.guicao.cv;

import cn.edu.fzu.sosd.guicao.cv.dto.EvaluationResult;

public class EvaluationReportGenerator {

    public static String generateEvaluationReport(EvaluationResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append("【卫生清洁效果评估】\n\n");

        double score = result.getCleanlinessScore();
        if (score < 50) {
            sb.append("本次清洁效果较差，总体评分为 ").append(score).append("/100。\n");
        } else if (score < 70) {
            sb.append("本次清洁效果一般，总体评分为 ").append(score).append("/100。\n");
        } else {
            sb.append("本次清洁效果良好，总体评分为 ").append(score).append("/100。\n");
        }

        sb.append("与打扫前相比：\n");

        double sharpChange = result.getDetails().getSharpnessChange();
        if (sharpChange > 0) {
            sb.append("✅ 图像清晰度有所提升（+").append(sharpChange).append("%），说明表面更加干净或光线更佳。\n");
        } else {
            sb.append("⚠️ 图像清晰度未明显改善（").append(sharpChange).append("%），可能需要重新擦拭或处理反光问题。\n");
        }

        int blobDecrease = result.getDetails().getBlobDecrease();
        if (blobDecrease == 0) {
            sb.append("❌ 未检测到斑点减少，可能存在残留污渍或垃圾未清理。\n");
        } else {
            sb.append("✅ 清理了 ").append(blobDecrease).append(" 个可见斑点，整体环境更整洁。\n");
        }

        double textureChange = result.getDetails().getTextureSimplified();
        if (textureChange == 0) {
            sb.append("🔸 纹理复杂度无明显变化，地面/桌面可能仍有杂物或图案干扰。\n");
        } else {
            sb.append("🔸 纹理复杂度降低 ").append(textureChange).append("%，说明环境变得更加规整。\n");
        }

        double colorSim = result.getDetails().getColorSimilarity();
        if (colorSim < 0.6) {
            sb.append("⚠️ 打扫前后颜色差异较大，可能是光照不均或存在新污染区域。\n");
        } else {
            sb.append("✅ 打扫前后颜色一致性较好，说明整体视觉环境较为稳定。\n");
        }

        sb.append("\n建议：继续优化清洁策略，重点关注斑点清除和图像清晰度提升，以提高整体清洁质量。");

        return sb.toString();
    }
}
