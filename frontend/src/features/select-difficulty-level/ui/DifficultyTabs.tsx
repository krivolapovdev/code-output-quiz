import { Tabs } from "antd";
import { useQuizStore } from "@/shared/lib/store";
import { useSupportedDifficultyLevels } from "../model";

export const DifficultyTabs = () => {
  const { loading, difficultyLevels } = useSupportedDifficultyLevels();
  const { difficultyLevel, setDifficultyLevel } = useQuizStore();

  if (loading) {
    return <div>Loading tabs...</div>;
  }

  const items = difficultyLevels.map(lvl => ({
    key: lvl,
    label: lvl.charAt(0).toUpperCase() + lvl.slice(1).toLowerCase(),
    style: { padding: "0" }
  }));

  return (
    <div className="flex justify-end">
      <Tabs
        type="card"
        onChange={key => setDifficultyLevel(key)}
        activeKey={difficultyLevel}
        items={items}
        tabBarStyle={{ margin: 0, borderRadius: 0 }}
      />
    </div>
  );
};
